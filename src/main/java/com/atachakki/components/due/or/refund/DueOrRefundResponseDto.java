package com.atachakki.components.due.or.refund;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DueOrRefundResponseDto(
        Long id,
        Long customerId,
        BigDecimal amount,
        DueRefundType type,
        DueRefundStatus status,
        Long addedById,
        String addedByName,
        Long updatedById,
        String updatedByName,
        LocalDate date,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
