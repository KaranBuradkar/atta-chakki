package com.atachakki.components.shop;

import com.atachakki.components.address.Address;
import com.atachakki.components.address.AddressDto;
import com.atachakki.components.address.AddressRepository;
import com.atachakki.components.staff.ShopStaff;
import com.atachakki.components.staff.ShopStaffMapper;
import com.atachakki.components.permissions.StaffPermission;
import com.atachakki.entity.User;
import com.atachakki.entity.UserDetails;
import com.atachakki.entity.type.Module;
import com.atachakki.entity.type.PermissionLevel;
import com.atachakki.entity.type.StaffRole;
import com.atachakki.exception.entityNotFound.ShopIdNotFoundException;
import com.atachakki.exception.entityNotFound.UserDetailsNotFoundException;
import com.atachakki.repository.ShopStaffRepository;
import com.atachakki.repository.StaffPermissionRepository;
import com.atachakki.repository.UserDetailsRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    private static final Logger log = LoggerFactory.getLogger(ShopServiceImpl.class);
    private final ShopRepository shopRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final AddressRepository addressRepository;
    private final ShopMapper shopMapper;
    private final ShopStaffRepository shopStaffRepository;
    private final StaffPermissionRepository staffPermissionRepository;
    private final ShopStaffMapper shopStaffMapper;

    public ShopServiceImpl(
            ShopMapper shopMapper,
            ShopRepository shopRepository,
            AddressRepository addressRepository,
            ShopStaffRepository shopStaffRepository,
            UserDetailsRepository userDetailsRepository,
            StaffPermissionRepository staffPermissionRepository,
            ShopStaffMapper shopStaffMapper
    ) {
        this.shopRepository = shopRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.addressRepository = addressRepository;
        this.shopMapper = shopMapper;
        this.shopStaffRepository = shopStaffRepository;
        this.staffPermissionRepository = staffPermissionRepository;
        this.shopStaffMapper = shopStaffMapper;
    }

    @Override
    public List<ShopShortResponseDto> getAllShops(Long userDetailsId) {
        List<ShopStaff> shops = shopStaffRepository.findByUserDetailIdAndActiveTrue(userDetailsId);
        return shops.stream()
                .map(shopStaffMapper::toShortResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public ShopResponseDto create(ShopRequestDto requestDto) {

        // 1. Get UserDetails
        UserDetails userDetails = resolveUserDetails();
        if (userDetails == null) {
            log.error("no user detail found");
            throw new UserDetailsNotFoundException("UserDetails not found");
        }

        // 2. Resolve address
        Address address = resolveShopAddress(requestDto.getAddressDto());

        // 3. resolve shop
        Shop shop = new Shop(
                requestDto.getName(),
                requestDto.getPhoneNo(),
                requestDto.getEmail(),
                requestDto.getLocationUrl(),
                address,
                userDetails,
                ShopStatus.ACTIVE
        );
        Shop savedShop = shopRepository.save(shop);

        // 4. resolve shop staff
        ShopStaff owner = new ShopStaff(
                shop, userDetails, StaffRole.OWNER
        );

        owner.setAddedBy(userDetails);
        ShopStaff saveShopStaff = shopStaffRepository.save(owner);
        savedShop.addShopStaff(saveShopStaff);

        // 5. resolve staff permissions
        List<StaffPermission> savedStaffPermissions = staffPermissionRepository
                .saveAll(getShopkeeperPermissions(saveShopStaff));
        savedStaffPermissions.forEach(saveShopStaff::addPermission);

        // 6. return dto
        return shopMapper.toResponseDto(savedShop);
    }

    @Override
    public ShopResponseDto updateShopFields(Long shopId, ShopRequestDto requestDto) {
        Shop shop = fetchShopByShopId(shopId);
        if (validateString(requestDto.getName(), shop.getName())) {
            shop.setName(requestDto.getName());
        }
        if (validateString(requestDto.getEmail(), shop.getEmail())) {
            shop.setEmail(requestDto.getEmail());
        }
        if (validateString(requestDto.getPhoneNo(), shop.getPhoneNo())) {
            shop.setPhoneNo(requestDto.getPhoneNo());
        }
        if (validateString(requestDto.getLocationUrl(), shop.getLocationUrl())) {
            shop.setLocationUrl(requestDto.getLocationUrl());
        }
        if (requestDto.getAddressDto() != null) {
            shop.setAddress(updateAddress(requestDto.getAddressDto(), shop.getAddress()));
        }
        Shop updated = shopRepository.save(shop);
        return shopMapper.toResponseDto(updated);
    }

    private Address updateAddress(@Valid AddressDto req, Address address) {
        address.setLandmark(req.getLandmark());
        address.setCity(req.getCity());
        address.setDistrict(req.getDistrict());
        address.setState(req.getLandmark());
        address.setCountry(req.getCountry());
        address.setPostalCode(req.getPostalCode());
        return addressRepository.save(address);
    }

    private boolean validateString(String request, String existing) {
        return request != null && !request.isBlank() && !request.equals(existing);
    }

    @Override
    @Transactional
    public void deleteShop(Long shopId) {
        if (shopRepository.existsById(shopId)) {
            shopRepository.deleteById(shopId);
        } else throw new ShopIdNotFoundException(shopId);
    }

    @Override
    @Transactional
    public ShopResponseDto updateShopStatus(Long shopId, ShopStatus status) {
        Shop shop = fetchShopByShopId(shopId);
        shop.setStatus(status);
        return shopMapper.toResponseDto(shop);
    }

    @Override
    public ShopResponseDto getShopDetails(Long shopId) {
        Shop shop = fetchShopByShopId(shopId);
        return shopMapper.toResponseDto(shop);
    }

    private Shop fetchShopByShopId(Long shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> {
                    log.warn("shop not found");
                    return new ShopIdNotFoundException(shopId);
                });
    }

    private List<StaffPermission> getShopkeeperPermissions(ShopStaff shopStaff) {
        return List.of(
                new StaffPermission(shopStaff, Module.CUSTOMER, PermissionLevel.FULL),
                new StaffPermission(shopStaff, Module.ORDER, PermissionLevel.FULL),
                new StaffPermission(shopStaff, Module.PAYMENT, PermissionLevel.FULL),
                new StaffPermission(shopStaff, Module.SHOP, PermissionLevel.FULL),
                new StaffPermission(shopStaff, Module.ORDER_ITEM_PRICE, PermissionLevel.FULL),
                new StaffPermission(shopStaff, Module.STAFF, PermissionLevel.FULL)
        );
    }

    private Address resolveShopAddress(AddressDto ad) {
        return addressRepository.save(new Address(
                ad.getLandmark(),
                ad.getCity(),
                ad.getDistrict(),
                ad.getState(),
                ad.getCountry(),
                ad.getPostalCode()
        ));
    }

    private UserDetails resolveUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return userDetailsRepository.findByUser(user)
                .orElse(null);
    }
}
