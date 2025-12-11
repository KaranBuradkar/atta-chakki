package com.atachakki.security;

import com.atachakki.repository.ShopStaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    private final ShopStaffRepository shopStaffRepository;

    public PermissionService(ShopStaffRepository shopStaffRepository) {
        this.shopStaffRepository = shopStaffRepository;
    }

    public List<String> getPermissions(Long userDetailsId, Long shopId) {
//        ShopStaff staff = shopStaffRepository.findByUserDetailsIdAndShopId(userDetailsId, shopId)
//                .orElseThrow();

        return List.of();
    }
}
