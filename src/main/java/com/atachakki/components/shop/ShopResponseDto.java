package com.atachakki.components.shop;

import com.atachakki.components.address.AddressDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ShopResponseDto(
        @JsonProperty(value = "shop_id") Long id,
        @JsonProperty(value = "shop_name") String name,
        @JsonProperty(value = "shop_owner") String owner,
        @JsonProperty(value = "shop_phone_no") String phoneNo,
        @JsonProperty(value = "shop_email") String email,
        @JsonProperty(value = "shop_status") ShopStatus status,
        @JsonProperty(value = "shop_location_url") String locationUrl,
        @JsonProperty(value = "shop_address") AddressDto addressDto
) {
}
