package com.atachakki.security;

import com.atachakki.components.shop.Shop;
import com.atachakki.components.shop.ShopStatus;
import com.atachakki.exception.businessLogic.ShopClosedException;
import com.atachakki.exception.entityNotFound.ShopIdNotFoundException;
import com.atachakki.components.shop.ShopRepository;
import com.atachakki.repository.UserDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private static final Logger log = LoggerFactory.getLogger(SecurityService.class);
    private final UserDetailsRepository userDetailsRepository;
    private final ShopRepository shopRepository;

    public SecurityService(
            UserDetailsRepository userDetailsRepository,
            ShopRepository shopRepository
    ) {
        this.userDetailsRepository = userDetailsRepository;
        this.shopRepository = shopRepository;
    }

    public boolean isValidUserDetailsToUser(Long userId, Long userDetailsId) {
        return resolveUserAndUserDetailsId(userId, userDetailsId);
    }

    private boolean resolveUserAndUserDetailsId(Long userId, Long userDetailsId) {
        return userDetailsRepository.existsByIdAndUserId(userDetailsId, userId)
                .orElseThrow(() -> {
                    log.warn("unauthorize user access denied with userId - {}", userId);
                    return new AccessDeniedException("unauthorize user access denied with userId - "+userId);
                });
    }

    public boolean isShopActive(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> {
                    log.warn("shop id not found");
                    return new ShopIdNotFoundException(shopId);
                });
        if (shop.getStatus().equals(ShopStatus.CLOSED) || shop.getStatus().equals(ShopStatus.SUSPENDED)) {
            log.warn("shop is closed or deactivated");
            throw new ShopClosedException("shop is closed or deactivated");
        }
        if (shop.getStatus().equals(ShopStatus.MAINTENANCE)) {
            log.warn("shop under {}", ShopStatus.MAINTENANCE.name());
            throw new ShopClosedException("shop under "+ShopStatus.MAINTENANCE.name());
        }
        return shop.getStatus().equals(ShopStatus.ACTIVE);
    }
}
