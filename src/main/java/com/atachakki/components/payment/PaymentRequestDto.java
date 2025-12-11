package com.atachakki.components.payment;

import com.atachakki.entity.type.PaymentStatus;
import com.atachakki.validation.PriceFormat;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequestDto(
        @PriceFormat(message = "Invalid amount, It cannot be null, negative and more than 2 decimal places")
        BigDecimal amount,
        @NotNull(message = "Payment mode is required") PaymentMode mode,
        @NotNull(message = "Payment mode is required") PaymentStatus status
) {}
