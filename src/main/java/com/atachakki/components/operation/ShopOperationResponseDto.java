package com.atachakki.components.operation;

import java.time.LocalDateTime;

public record ShopOperationResponseDto(
        Long id,
        String shopName,
        String message,
        String details,
        String sender,
        LocalDateTime createdAt
) {}
