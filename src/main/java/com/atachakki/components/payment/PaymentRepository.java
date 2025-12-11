package com.atachakki.components.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByIdAndDeletedFalseAndCustomerShopId(Long paymentId, Long shopId);

    Page<Payment> findByDeletedFalseAndCustomerShopIdAndCustomerId(Long shopId, Long customerId, PageRequest of);
}