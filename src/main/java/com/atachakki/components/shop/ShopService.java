package com.atachakki.components.shop;

import com.atachakki.security.authorizationValidation.IsAdminOrShopOwnerOrShopkeeper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShopService {

    @PreAuthorize(value = "hasAnyRole('ADMIN') OR " +
            "@securityService.isValidUserDetailsToUser(authentication.principal.id, #userDetailsId)")
    List<ShopShortResponseDto> getAllShops(Long userDetailsId);

    @PreAuthorize(value = "@permissionGuard.check(#shopId, 'SHOP', 'READ')")
    ShopResponseDto getShopDetails(Long shopId);

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'CLIENT', 'MANAGEMENT')")
    ShopResponseDto create(ShopRequestDto requestDto);

    @IsAdminOrShopOwnerOrShopkeeper
    ShopResponseDto updateShopFields(Long shopId, ShopRequestDto requestDto);

    @IsAdminOrShopOwnerOrShopkeeper
    ShopResponseDto updateShopStatus(Long shopId, ShopStatus status);

    @PreAuthorize(value = "hasAnyRole('ADMIN') OR hasAuthority('SHOP_'+#shopId+'_OWNER')")
    void deleteShop(Long shopId);
}
