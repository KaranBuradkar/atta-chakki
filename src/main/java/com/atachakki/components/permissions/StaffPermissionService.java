package com.atachakki.components.permissions;

import com.atachakki.security.authorizationValidation.IsAdminOrShopOwnerOrShopkeeper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StaffPermissionService {

    @PreAuthorize(value = "@permissionGuard.check(#shopId, 'STAFF_PERMISSION', 'READ')")
    Page<StaffPermissionResponseDto> findStaffPermissions(
            Long shopId, Long staffId, Integer page, Integer size, String direction, String[] sort);

    @IsAdminOrShopOwnerOrShopkeeper
    List<StaffPermissionResponseDto> create(
            Long shopId, Long staffId, @Valid List<StaffPermissionRequestDto> requestDto);

    @IsAdminOrShopOwnerOrShopkeeper
    void delete(Long shopId, Long permissionId);
}
