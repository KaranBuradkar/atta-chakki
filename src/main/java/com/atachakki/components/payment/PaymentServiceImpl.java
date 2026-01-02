package com.atachakki.components.payment;

import com.atachakki.components.customer.Customer;
import com.atachakki.components.customer.CustomerRepository;
import com.atachakki.components.due.or.refund.*;
import com.atachakki.components.operation.ShopOperationService;
import com.atachakki.components.order.Order;
import com.atachakki.components.order.OrderRepository;
import com.atachakki.components.staff.ShopStaff;
import com.atachakki.entity.User;
import com.atachakki.entity.type.Module;
import com.atachakki.entity.type.PaymentStatus;
import com.atachakki.exception.entityNotFound.CustomerBelongToShopException;
import com.atachakki.exception.entityNotFound.PaymentNotFoundException;
import com.atachakki.exception.entityNotFound.StaffNotFoundException;
import com.atachakki.exception.entityNotFound.UserNotFoundException;
import com.atachakki.exception.validation.PaymentValidationFailed;
import com.atachakki.repository.ShopStaffRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final ShopStaffRepository shopStaffRepository;
    private final CustomerRepository customerRepository;
    private final ShopOperationService shopOperationService;
    private final OrderRepository orderRepository;
    private final DueOrRefundRepository dueOrRefundRepository;
    private final DueOrRefundMapper dueOrRefundMapper;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            PaymentMapper paymentMapper,
            ShopStaffRepository shopStaffRepository,
            CustomerRepository customerRepository,
            ShopOperationService shopOperationService,
            OrderRepository orderRepository,
            DueOrRefundRepository dueOrRefundRepository,
            DueOrRefundMapper dueOrRefundMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.shopStaffRepository = shopStaffRepository;
        this.customerRepository = customerRepository;
        this.shopOperationService = shopOperationService;
        this.orderRepository = orderRepository;
        this.dueOrRefundRepository = dueOrRefundRepository;
        this.dueOrRefundMapper = dueOrRefundMapper;
    }

    @Override
    public Page<PaymentResponseShortDto> findPayments(
            Long shopId, Long customerId, Integer page,
            Integer size, String direction, String sort
    ) {
        Sort.Direction dir = ("asc".equalsIgnoreCase(direction)) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<Payment> paymentsPage = paymentRepository
                .findByDeletedFalseAndCustomerShopIdAndCustomerId(
                        shopId, customerId, PageRequest.of(page, size, dir, sort)
                );
        return paymentsPage.map(paymentMapper::toResponseShortDto);
    }

    @Override
    public PaymentResponseDto findPayment(Long shopId, Long paymentId) {
        Payment payment = fetchPayment(paymentId, shopId);
        return paymentMapper.toResponseDto(payment);
    }

    @Override
    @Transactional
    public PaymentResponseDto create(Long shopId, Long customerId, PaymentBundleRequestDto requestDto) {

        Page<Order> orderPage = validateFetchOrders(shopId, customerId, requestDto.getOrderIds());

        PaymentRequestDto pay = requestDto.getPaymentRequestDto();

        DueOrRefundRequestDto dueOrRefundRequestDto = requestDto.getDueOrRefundRequestDto();
        validatePayment(pay, orderPage, dueOrRefundRequestDto);

        ShopStaff staff = getCurrentStaff(shopId);
        Customer customer = fetchCustomer(customerId, shopId);

        Payment entity = paymentMapper.toEntity(pay);
        entity.setReceiver(staff);
        entity.setCustomer(customer);
        Payment newPayment = paymentRepository.save(entity);

        // update orders payment status
        orderPage.forEach(o -> o.setPaymentStatus(PaymentStatus.PAID));
        orderRepository.saveAll(orderPage.getContent());

        if (dueOrRefundRequestDto != null) {
            DueOrRefund dueOrRefund = dueOrRefundMapper.toEntity(dueOrRefundRequestDto);
            dueOrRefund.setCustomer(customer);
            dueOrRefund.setAddedBy(staff);
            dueOrRefundRepository.save(dueOrRefund);
        }

        PaymentResponseDto responseDto = paymentMapper.toResponseDto(newPayment);
        shopOperationService.createModule(shopId, newPayment.getReceiver().getId(),
                Module.PAYMENT, responseDto.id(), responseDto.toString());
        return responseDto;
    }

    private void validatePayment(
            PaymentRequestDto pay, Page<Order> orderPage,
            DueOrRefundRequestDto dueOrRefundRequestDto
    ) {
        BigDecimal total = orderPage
                .stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        final BigDecimal lastOrderAmount = orderPage.getContent()
                .get(orderPage.getContent().size() - 1)
                .getTotalAmount();

        validateDueOrRefund(total, pay.amount(), lastOrderAmount, dueOrRefundRequestDto);
    }

    private Page<Order> validateFetchOrders(Long shopId, Long customerId, List<Long> orderIds) {
        PageRequest pageRequest = PageRequest.of(0,
                orderIds.size(), Sort.Direction.ASC, "orderDate");
        Page<Order> orderPage = orderRepository
                .findAllByIdInAndCustomerIdAndPaymentStatusAndCustomerShopId(
                        orderIds,
                        customerId,
                        PaymentStatus.PENDING,
                        shopId,
                        pageRequest
                );
        if (isInvalidFetchedOrders(orderIds, orderPage)) {
            log.warn("{fetchedOrderPage, provided orderIds size is different}");
            throw new PaymentValidationFailed("fetched orders and provided orderIds are not same");
        }
        return orderPage;
    }

    private void validateDueOrRefund(
            BigDecimal total,
            BigDecimal payAmount,
            BigDecimal lastOrderAmount,
            DueOrRefundRequestDto dueOrRefundRequestDto
    ) {
        int compare = total.compareTo(payAmount);

        if (compare > 0) {
            if (dueOrRefundRequestDto == null) {
                log.warn("If dueOrRefundRequestDto is not NULL");
                throw new PaymentValidationFailed("Required DueOrRefund");
            }
            // total > payAmount → DUE
            BigDecimal dueAmount = total.subtract(payAmount);
            if (dueAmount.compareTo(dueOrRefundRequestDto.amount()) != 0) {
                throw new PaymentValidationFailed("Due calculation error",
                        String.format("Due expected amount=%s and provided amount=%s",
                                dueAmount, dueOrRefundRequestDto.amount()));
            }

            if (dueAmount.compareTo(lastOrderAmount) >= 0) {
                log.warn("Mistake in order selection");
                throw new PaymentValidationFailed("Amount is not calculated properly",
                        String.format("Due amount Rs.%s must be less than last order amount Rs.%s that case dis select last order",
                                dueAmount, lastOrderAmount));
            }
            if (dueOrRefundRequestDto.type() != DueRefundType.DUE) {
                throw new PaymentValidationFailed("This type is due");
            }
            if(dueAmount.compareTo(dueOrRefundRequestDto.amount()) < 0) {
                log.warn("Invalid DUE calculation");
                throw new PaymentValidationFailed("Amount is not calculated properly",
                        String.format("Due amount expected=%s but provided amount=%s",
                                dueAmount, dueOrRefundRequestDto.amount()));
            }
            return;
        } else if (compare < 0) {
            if (dueOrRefundRequestDto == null) {
                log.warn("DueOrRefundRequestDto is not NULL");
                throw new PaymentValidationFailed("Required DueOrRefundRequest");
            }
            // total < payAmount → REFUND
            BigDecimal refundAmount = payAmount.subtract(total);
            if (refundAmount.compareTo(dueOrRefundRequestDto.amount()) != 0) {
                log.warn("Mistake in REFUND calculation");
                throw new PaymentValidationFailed("Amount is not calculated properly",
                        String.format("Refund amount expected=%s but provided amount=%s",
                                refundAmount, dueOrRefundRequestDto.amount()));
            }

            if (dueOrRefundRequestDto.type() != DueRefundType.REFUND) {
                throw new PaymentValidationFailed("This type is Refund");
            }
            return;
        }

        // If total == payAmount → should be no DUE or REFUND
        if (dueOrRefundRequestDto != null) {
            log.warn("No due/refund should exist when total == payAmount");
            throw new PaymentValidationFailed("Payment type mismatch with zero balance");
        }
    }

    private boolean isInvalidFetchedOrders(List<Long> orderIds, Page<Order> orderPage) {
        List<Long> fetchedIds = orderPage.stream().map(Order::getId).toList();

        if (fetchedIds.size() != orderIds.size() ||
                !new HashSet<>(fetchedIds).containsAll(orderIds)) {
            throw new PaymentValidationFailed("Fetched orders mismatch with provided IDs");
        }
        return orderPage.getContent().size() != orderIds.size() || !new HashSet<>(orderIds).containsAll(fetchedIds);
    }

    @Transactional
    @Override
    public PaymentResponseDto update(Long shopId, Long paymentId, PaymentRequestDto requestDto) {
        Payment payment = fetchPayment(paymentId, shopId);
        PaymentResponseDto before = paymentMapper.toResponseDto(payment);
        StringBuilder fields = new StringBuilder("[");
        if (requestDto.mode() != null
                && !payment.getMode().equals(requestDto.mode())
        ) {
            payment.setMode(requestDto.mode());
            fields.append(", mode");
        }
        if (!payment.getAmount().equals(requestDto.amount())) {
            payment.setAmount(requestDto.amount());
            fields.append(", amount");
        }
        Payment updatedPayment = paymentRepository.save(payment);
        PaymentResponseDto responseDto = paymentMapper.toResponseDto(updatedPayment);
        shopOperationService.updateModule(shopId, getCurrentStaff(shopId).getId(), Module.PAYMENT, responseDto.id(),
                fields.toString(), before.toString(), responseDto.toString());
        return responseDto;
    }

    @Override
    @Transactional
    public void deleteById(Long shopId, Long paymentId) {
        Payment payment = fetchPayment(paymentId, shopId);
        PaymentResponseDto before = paymentMapper.toResponseDto(payment);
        payment.setDeleted(true);
        paymentRepository.save(payment);
        shopOperationService.deleteModule(shopId, getCurrentStaff(shopId).getId(), Module.PAYMENT,
                before.id(), before.toString());
    }

    // Utils methods
    private ShopStaff getCurrentStaff(Long shopId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = ((User) auth.getPrincipal()); // username stored in token
        if (user == null) {
            log.error("User by-passed security");
            throw new UserNotFoundException("UnAuthorize access denied",
                    "Without authority you enter in application");
        }
        return shopStaffRepository.findByShopIdAndUserDetailUserIdAndActiveTrue(shopId, user.getId())
                .orElseThrow(() -> new StaffNotFoundException("Staff not found for this shop"));
    }

    private Customer fetchCustomer(Long customerId, Long shopId) {
        return customerRepository.findByIdAndShopId(customerId, shopId)
                .orElseThrow(() -> {
                    log.warn("Customer not found");
                    return new CustomerBelongToShopException(customerId, shopId);
                });
    }

    private Payment fetchPayment(Long paymentId, Long shopId) {
        return paymentRepository.findByIdAndDeletedFalseAndCustomerShopId(paymentId, shopId)
                .orElseThrow(() -> {
                    log.warn("Payment not found");
                    return new PaymentNotFoundException(paymentId);
                });
    }
}
