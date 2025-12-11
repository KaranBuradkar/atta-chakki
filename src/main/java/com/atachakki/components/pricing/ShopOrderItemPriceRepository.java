package com.atachakki.components.pricing;

import com.atachakki.entity.type.QuantityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopOrderItemPriceRepository extends JpaRepository<ShopOrderItemPrice, Long> {

  Page<ShopOrderItemPrice> findByShopId(Long shopId, PageRequest of);

  Optional<ShopOrderItemPrice> findByShopIdAndOrderItemNameAndQuantityTypeAndAvailableTrue(
            Long shopId, String orderItemName, QuantityType quantityType);

  Optional<ShopOrderItemPrice> findByIdAndShopIdAndAvailableTrue(Long orderItemPriceId, Long shopId);
}