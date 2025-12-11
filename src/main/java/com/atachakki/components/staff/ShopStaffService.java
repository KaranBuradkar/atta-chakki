package com.atachakki.components.staff;

import com.atachakki.security.authorizationValidation.IsAdminOrShopOwnerOrShopkeeper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public interface ShopStaffService {

    @PreAuthorize(value = "@permissionGuard.check(#shopId, 'STAFF', 'READ')")
    Page<ShopStaffResponseDto> findShopStaffs(
            Long shopId, Integer page, Integer size, String direction, String sort);

    @PreAuthorize(value = "@permissionGuard.check(#shopId, 'STAFF', 'READ')")
    ShopStaffResponseDto findShopStaff(Long shopId, Long shopStaffId);

    @IsAdminOrShopOwnerOrShopkeeper
    ShopStaffResponseDto create(Long shopId, @Valid ShopStaffRequestDto requestDto);

    @IsAdminOrShopOwnerOrShopkeeper
    ShopStaffResponseDto update(Long shopId, Long shopStaffId, @Valid ShopStaffRequestDto requestDto);

    @IsAdminOrShopOwnerOrShopkeeper
    void deleteById(Long shopId, Long shopStaffId);
}
