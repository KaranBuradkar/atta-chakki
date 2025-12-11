package com.atachakki.components.order;

import com.atachakki.entity.type.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByCustomerId(Long customerId, PageRequest of);

    @Query("select o from Order o " +
            "where o.id = ?1 and o.customer.shop.id = ?2")
    Optional<Order> findByIdAndShopId(Long orderId, Long shopId);

    Page<Order> findAllByCustomerId(Long customerId, PageRequest of);

    @Query(value = "select sum(o.totalAmount) from Order o where o.customer.shop.id = ?1")
    BigDecimal findTotalDebt(Long shopId);

    Page<Order> findAllByIdInAndCustomerIdAndPaymentStatusAndCustomerShopId(
            List<Long> orderIds, Long customerId, PaymentStatus status, Long shopId, PageRequest pageRequest);
}