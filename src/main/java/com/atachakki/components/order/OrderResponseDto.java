package com.atachakki.components.order;

import com.atachakki.entity.type.PaymentStatus;
import com.atachakki.entity.type.QuantityType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OrderResponseDto(
        Long id,
        Long orderItemId,
        String orderItemName,
        Integer quantity,
        QuantityType quantityType,
        BigDecimal totalAmount,
        PaymentStatus paymentStatus,
        Long addedById,
        String addedByName,
        Long updatedById,
        String updatedByName,
        LocalDate orderDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
