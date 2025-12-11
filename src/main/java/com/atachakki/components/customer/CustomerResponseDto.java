package com.atachakki.components.customer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public record CustomerResponseDto (
    @JsonProperty(value = "customer_id") Long id,
    @JsonProperty(value = "customer_name") String name,
    @JsonProperty(value = "customer_email") String email,
    @JsonProperty(value = "customer_specification") String specification,
    @JsonProperty(value = "customer_block") Boolean block,
    @JsonProperty(value = "customer_created_at") LocalDateTime createdAt
) {}
