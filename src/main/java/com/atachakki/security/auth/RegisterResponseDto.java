package com.atachakki.security.auth;

import com.atachakki.entity.type.AuthProvider;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterResponseDto {

    @JsonProperty(value = "user_details_id") private Long userDetailsId;
    private String name;
    private String email;
    @JsonProperty(value = "phone_no") private String phoneNo;
    @JsonProperty(value = "profile_url") private String profileUrl;
    @JsonProperty(value = "provider") private AuthProvider provider;
    @JsonProperty(value = "access_token") private String accessToken;
    @JsonProperty(value = "refresh_token") private String refreshToken;

    public RegisterResponseDto() {}

    public RegisterResponseDto(
            Long userDetailsId,
            String name,
            String email,
            String phoneNo,
            String profileUrl,
            AuthProvider provider,
            String accessToken,
            String refreshToken
    ) {
        this.userDetailsId = userDetailsId;
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.profileUrl = profileUrl;
        this.provider = provider;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Long getUserDetailsId() {
        return userDetailsId;
    }

    public void setUserDetailsId(Long userDetailsId) {
        this.userDetailsId = userDetailsId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
