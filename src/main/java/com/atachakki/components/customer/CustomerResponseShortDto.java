package com.atachakki.components.customer;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CustomerResponseShortDto(
        @JsonProperty("customer_id") Long id,
        @JsonProperty("customer_name") String name,
        @JsonProperty("customer_block") Boolean block
) {}
