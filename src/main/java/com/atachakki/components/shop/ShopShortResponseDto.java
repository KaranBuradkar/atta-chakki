package com.atachakki.components.shop;

import com.atachakki.entity.type.StaffRole;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ShopShortResponseDto (
    @JsonProperty(value = "shop_id") Long id,
    @JsonProperty(value = "shop_name") String name,
    @JsonProperty(value = "shop_owner") String owner,
    @JsonProperty(value = "shop_staff_role") StaffRole staffRole

) {
    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", staffRole=" + staffRole +
                '}';
    }
}
