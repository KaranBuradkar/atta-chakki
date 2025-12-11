package com.atachakki.components.permissions;

import com.atachakki.entity.type.PermissionLevel;
import com.atachakki.entity.type.Module;

public record StaffPermissionResponseDto(
        Long id, Module module, PermissionLevel level
) {
}
