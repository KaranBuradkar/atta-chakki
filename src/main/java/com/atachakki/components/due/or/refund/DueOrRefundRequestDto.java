package com.atachakki.components.due.or.refund;

import com.atachakki.validation.PriceFormat;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DueOrRefundRequestDto(
        @PriceFormat
        BigDecimal amount,
        @NotNull DueRefundType type,
        @NotNull DueRefundStatus status,
        @NotNull LocalDate date
) {}
