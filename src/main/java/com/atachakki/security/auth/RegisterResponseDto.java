package com.atachakki.security.auth;

import com.atachakki.entity.type.AuthProvider;
import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterResponseDto (
    @JsonProperty(value = "user_details_id")  Long userDetailsId,
     String name,
     String username,
    @JsonProperty(value = "phone_no")  String phoneNo,
    @JsonProperty(value = "profile_url")  String profileUrl,
    @JsonProperty(value = "provider")  AuthProvider provider,
    @JsonProperty(value = "access_token")  String accessToken,
    @JsonProperty(value = "refresh_token")  String refreshToken
) {}