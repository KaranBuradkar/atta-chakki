package com.atachakki.components.customer;

import com.atachakki.components.operation.ShopOperationService;
import com.atachakki.components.shop.Shop;
import com.atachakki.components.staff.ShopStaff;
import com.atachakki.entity.User;
import com.atachakki.entity.type.Module;
import com.atachakki.exception.entityNotFound.CustomerIdNotFoundException;
import com.atachakki.exception.entityNotFound.ShopIdNotFoundException;
import com.atachakki.exception.entityNotFound.StaffNotFoundException;
import com.atachakki.components.shop.ShopRepository;
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

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private final CustomerRepository customerRepository;
    private final ShopRepository shopRepository;
    private final CustomerMapper customerMapper;
    private final ShopStaffRepository shopStaffRepository;
    private final ShopOperationService shopOperationService;

    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            ShopRepository shopRepository,
            CustomerMapper customerMapper,
            ShopStaffRepository shopStaffRepository,
            ShopOperationService shopOperationService
    ) {
        this.customerRepository = customerRepository;
        this.shopRepository = shopRepository;
        this.customerMapper = customerMapper;
        this.shopStaffRepository = shopStaffRepository;
        this.shopOperationService = shopOperationService;
    }

    @Override
    public Page<CustomerResponseShortDto> findCustomers(
            Long shopId, Integer page, Integer size,
            String direction, String sort,
            String name
    ) {
        Sort.Direction dir = ("asc".equalsIgnoreCase(direction)) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<Customer> customerPage;
        if (name != null && !name.isBlank()) {
            customerPage = customerRepository.findByShopIdAndNameContainingAndDeletedFalse(
                    shopId, name, PageRequest.of(page, size, dir, sort));
        } else {
            customerPage = customerRepository
                    .findByShopIdAndDeletedFalse(shopId, PageRequest.of(page, size, dir, sort));
        }
        return customerPage.map(customerMapper::toResponseShortDto);
    }

    @Override
    public CustomerResponseDto findCustomer(Long shopId, Long customerId) {
        Customer customer = fetchCustomerById(shopId, customerId);
        return customerMapper.toResponseDto(customer);
    }

    @Override
    @Transactional
    public CustomerResponseDto create(
            Long shopId,
            CustomerRequestDto requestDto
    ) {
        Shop shop = fetchShopByShopId(shopId);
        ShopStaff staff = getCurrentStaff(shopId);

        Customer customer = customerMapper.toEntity(requestDto);
        customer.setShop(shop);
        customer.setAddedBy(staff);
        customer.setBlock(false);
        customer.setDeleted(false);
        Customer response = customerRepository.save(customer);
        CustomerResponseDto responseDto = customerMapper.toResponseDto(response);
        shopOperationService.createModule(shopId, Module.CUSTOMER, responseDto.id(), stringCustomer(responseDto));
        return responseDto;
    }

    @Override
    @Transactional
    public CustomerResponseDto updateCustomerBlockStatus(
            Long shopId, Long customerId, Boolean block
    ) {
        ShopStaff staff = getCurrentStaff(shopId);
        Customer customer = fetchCustomerById(shopId, customerId);

        CustomerResponseDto before = customerMapper.toResponseDto(customer);
        customer.setBlock(block);
        customer.setUpdatedBy(staff);
        Customer updatedCustomer = customerRepository.save(customer);
        CustomerResponseDto responseDto = customerMapper.toResponseDto(updatedCustomer);
        shopOperationService.updateModule(shopId, Module.CUSTOMER, responseDto.id(),
                "[block, updatedBy]", stringCustomer(before), stringCustomer(responseDto));
        return responseDto;
    }

    @Override
    @Transactional
    public CustomerResponseDto updateCustomerFields(
            Long shopId, Long customerId,
            CustomerRequestDto requestDto
    ) {
        Customer customer = fetchCustomerById(shopId, customerId);
        CustomerResponseDto before = customerMapper.toResponseDto(customer);
        ShopStaff staff = getCurrentStaff(customer);
        StringBuilder fields = new StringBuilder("[");

        if (validateString(requestDto.getName(), customer.getName())) {
            customer.setName(requestDto.getName());
            fields.append("name,");
        }
        if (validateString(requestDto.getEmail(), customer.getEmail())) {
            customer.setEmail(requestDto.getEmail());
            fields.append(", email");
        }
        if (validateString(requestDto.getSpecification(), customer.getSpecification())) {
            customer.setSpecification(requestDto.getSpecification());
            fields.append(", specification");
        }
        customer.setUpdatedBy(staff);
        fields.append(", updatedBy]");
        Customer saveCustomer = customerRepository.save(customer);
        CustomerResponseDto responseDto = customerMapper.toResponseDto(saveCustomer);
        shopOperationService.updateModule(shopId, Module.CUSTOMER, saveCustomer.getId(),
                fields.toString(), stringCustomer(before), stringCustomer(responseDto));
        return responseDto;
    }

    @Override
    @Transactional
    public void deleteById(Long shopId, Long customerId) {
        ShopStaff staff = getCurrentStaff(shopId);
        Customer customer = fetchCustomerById(shopId, customerId);
        customer.setUpdatedBy(staff);
        customer.setDeleted(true);
        Customer deleted = customerRepository.save(customer);
        CustomerResponseDto responseDto = customerMapper.toResponseDto(deleted);
        shopOperationService.deleteModule(shopId, Module.CUSTOMER,
                responseDto.id(), stringCustomer(responseDto));
    }

    @Override
    public Page<CustomerResponseDto> findAllCustomers(
            Long shopId, int page, int size, String direction, String name) {
        Sort.Direction dir = ("asc".equalsIgnoreCase(direction)) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<Customer> customers = customerRepository
                .findByShopIdAndDeletedFalse(shopId, PageRequest.of(page, size, dir, name));
        return customers.map(customerMapper::toResponseDto);
    }

    // Util methods
    private Customer fetchCustomerById(Long shopId, Long customerId) {
        return customerRepository.findByIdAndShopId(customerId, shopId)
                .orElseThrow(() -> {
                    log.warn("customer id not found");
                    return new CustomerIdNotFoundException(customerId);
                });
    }

    private ShopStaff getCurrentStaff(Customer customer) {
        List<ShopStaff> shopStaffs = customer.getShop().getShopStaffs();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((User) auth.getPrincipal()).getId(); // username stored in token

        List<ShopStaff> list = shopStaffs.stream()
                .filter(staff -> Objects.equals(staff.getUserDetail().getUser().getId(), userId))
                .toList();
        if (list.size() == 1) {
            return list.getFirst();
        }
        return null;
    }

    private Shop fetchShopByShopId(Long shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> {
                    log.warn("shopId not found");
                    return new ShopIdNotFoundException(shopId);
                }
        );
    }

    private ShopStaff getCurrentStaff(Long shopId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((User) auth.getPrincipal()).getId(); // username stored in token
        return shopStaffRepository.findByShopIdAndUserDetailUserIdAndActiveTrue(shopId, userId)
                .orElseThrow(() -> new StaffNotFoundException("Staff not found for this shop"));
    }

    private boolean validateString(String req, String exist) {
        return req != null && !req.isBlank() && !req.equals(exist);
    }

    private String stringCustomer(CustomerResponseDto c) {
        return String.format("{id=%s, name=%s, email=%s, specification=%s, block=%s, createdAt=%s}",
                c.id(), c.name(), c.email(), c.specification(), c.block(), c.createdAt());
    }
}
