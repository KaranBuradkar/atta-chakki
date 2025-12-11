package com.atachakki.components.permissions;

import com.atachakki.components.staff.ShopStaff;
import com.atachakki.entity.type.Module;
import com.atachakki.entity.type.PermissionLevel;
import jakarta.persistence.*;

@Entity
@Table(name = "staff_permission")
public class StaffPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_staff_id", nullable = false)
    private ShopStaff shopStaff;

    @Enumerated(EnumType.STRING)
    @Column(name = "module", nullable = false)
    private Module module;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_level", nullable = false)
    private PermissionLevel level;

    public StaffPermission() {}

    public StaffPermission(
            ShopStaff shopStaff,
            Module module,
            PermissionLevel level
    ) {
        this.shopStaff = shopStaff;
        this.module = module;
        this.level = level;
    }

    public ShopStaff getShopStaff() {
        return shopStaff;
    }

    public void setShopStaff(ShopStaff shopStaff) {
        this.shopStaff = shopStaff;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public PermissionLevel getLevel() {
        return level;
    }

    public void setLevel(PermissionLevel level) {
        this.level = level;
    }
}