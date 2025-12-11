package com.atachakki.repository;

import com.atachakki.components.permissions.StaffPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StaffPermissionRepository extends JpaRepository<StaffPermission, Long> {

    @Query("select distinct concat('SHOP_', p.shopStaff.shop.id, '_', p.module, '_', p.level) " +
            "from StaffPermission p " +
            "where p.shopStaff.shop.id =?1 " +
            "and p.shopStaff.userDetail.user.id =?2")
    List<String> findStaffAuthorityByShopId(Long shopId, Long userId);

    @Query("select distinct p from StaffPermission p " +
            "where p.shopStaff.userDetail.user.id =?1")
    List<StaffPermission> findStaffPermissionsByUserId(Long userId);

    @Query(value = "select distinct concat('SHOP_', p.shopStaff.shop.id, '_', p.module, '_', p.level) " +
            "from StaffPermission p where p.shopStaff.userDetail.user.id =?1")
    List<String> findStaffAuthority(Long userId);

    @Query("""
                select distinct concat(
                    'SHOP_', p.shopStaff.shop.id, '_',
                    p.shopStaff.staffRole
                )
                from StaffPermission p
                where p.shopStaff.userDetail.user.id = :userId
            """)
    List<String> findStaffRoles(@Param("userId") Long userId);

    Page<StaffPermission> findByShopStaffIdAndShopStaffShopId(Long staffId, Long shopId, PageRequest of);

    Optional<StaffPermission> findByIdAndShopStaffShopId(Long permissionId, Long shopId);

    List<StaffPermission> findByShopStaffIdAndShopStaffShopId(Long staffId, Long shopId);
}