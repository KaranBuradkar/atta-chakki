package com.atachakki.components.shop;

import com.atachakki.entity.type.StaffRole;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopShortResponseDto {

    @JsonProperty(value = "shop_id") private Long id;
    @JsonProperty(value = "shop_name") private String name;
    @JsonProperty(value = "shop_owner") private String owner;
    @JsonProperty(value = "shop_staff_role") private StaffRole staffRole;

    public ShopShortResponseDto() {}

    public ShopShortResponseDto(
            Long id, String name,
            String owner, StaffRole staffRole
    ) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.staffRole = staffRole;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public StaffRole getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(StaffRole staffRole) {
        this.staffRole = staffRole;
    }
}
