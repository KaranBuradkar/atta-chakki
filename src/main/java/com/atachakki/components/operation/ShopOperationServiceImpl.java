package com.atachakki.components.operation;

import com.atachakki.components.shop.Shop;
import com.atachakki.components.shop.ShopRepository;
import com.atachakki.components.staff.ShopStaff;
import com.atachakki.entity.User;
import com.atachakki.entity.type.Module;
import com.atachakki.entity.type.Operation;
import com.atachakki.exception.entityNotFound.NotificationIdNotFound;
import com.atachakki.exception.entityNotFound.ShopIdNotFoundException;
import com.atachakki.exception.entityNotFound.StaffNotFoundException;
import com.atachakki.repository.ShopStaffRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ShopOperationServiceImpl implements ShopOperationService {

    private static final Logger log = LoggerFactory.getLogger(ShopOperationServiceImpl.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private final ShopOperationRepository shopOperationRepository;
    private final ShopOperationMapper shopOperationMapper;
    private final ShopRepository shopRepository;
    private final ShopStaffRepository shopStaffRepository;

    public ShopOperationServiceImpl(
            ShopOperationRepository shopOperationRepository,
            ShopOperationMapper shopOperationMapper,
            ShopRepository shopRepository,
            ShopStaffRepository shopStaffRepository) {
        this.shopOperationRepository = shopOperationRepository;
        this.shopOperationMapper = shopOperationMapper;
        this.shopRepository = shopRepository;
        this.shopStaffRepository = shopStaffRepository;
    }

    private ShopStaff getCurrentStaff(Long shopId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((User) auth.getPrincipal()).getId(); // username stored in token
        return shopStaffRepository.findByShopIdAndUserDetailUserIdAndActiveTrue(shopId, userId)
                .orElseThrow(() -> new StaffNotFoundException("Staff not found for this shop"));
    }

    private void build(
            Long shopId, Long entityId, Operation operation,
            Module module, String changedFields,
            String beforeValues, String afterValues
    ) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> {
                    log.warn("Shop not found");
                    return new ShopIdNotFoundException(shopId);
                });
        ShopOperation shopOperation = new ShopOperation();
        shopOperation.setShop(shop);
        shopOperation.setSender(getCurrentStaff(shopId));
        shopOperation.setOperation(operation);
        shopOperation.setModule(module);
        shopOperation.setChangedFields(changedFields);
        shopOperation.setBeforeValues(beforeValues);
        shopOperation.setAfterValues(afterValues);
        shopOperation.setEntityId(entityId.toString());
        shopOperationRepository.save(shopOperation);
    }

    @Override
    public Page<ShopOperationResponseDto> findNotifications(
            Long shopId, Integer page, Integer size,
            String direction, String sort
    ) {
        Sort.Direction dir = ("asc".equalsIgnoreCase(direction)) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<ShopOperation> notifications = shopOperationRepository
                .findByShopId(shopId, PageRequest.of(page, size, dir, sort));
        return notifications.map(shopOperationMapper::toResponseDto);
    }

    @Override
    @Transactional
    @Async
    public void createModule(Long shopId, Module module, Long entityId, String afterValues) {
        build(shopId, entityId, Operation.CREATE, module, null, null, afterValues);
    }

    @Override
    @Transactional
    @Async
    public void updateModule(Long shopId, Module module, Long entityId,
                             String changedFields, String beforeValues, String afterValues) {
        build(shopId, entityId, Operation.UPDATE, module, changedFields, beforeValues, afterValues);
    }

    @Override
    @Transactional
    @Async
    public void deleteModule(Long shopId, Module module, Long entityId, String beforeValues) {
        build(shopId, entityId, Operation.DELETE, module,
                "Customer deleted", beforeValues, null);
    }

    @Override
    @Transactional
    public void delete(Long shopId, Long notificationId) {
        if (shopOperationRepository.existsByIdAndShopId(notificationId, shopId)) {
            shopOperationRepository.deleteById(notificationId);
        } else {
            log.warn("Notification not found");
            throw new NotificationIdNotFound(notificationId);
        }
    }
}
