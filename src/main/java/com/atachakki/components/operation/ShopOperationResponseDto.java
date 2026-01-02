package com.atachakki.components.operation;

import com.atachakki.entity.type.Module;
import com.atachakki.entity.type.Operation;

import java.time.LocalDateTime;

public record ShopOperationResponseDto(
        Long id,
        String shopName,
        String sender,
        Module module,
        String entityId,
        Operation operation,
        String changedFields,
        String beforeValues,
        String afterValues,
        LocalDateTime createdAt
) {}
