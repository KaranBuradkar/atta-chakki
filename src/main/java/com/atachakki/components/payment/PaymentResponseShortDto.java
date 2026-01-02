package com.atachakki.components.payment;

import com.atachakki.entity.type.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponseShortDto(
        Long id,
        Long customerId,
        BigDecimal amount,
        String mode,
        PaymentStatus status,
        String receiverName,
        LocalDateTime createdAt
) {
}
