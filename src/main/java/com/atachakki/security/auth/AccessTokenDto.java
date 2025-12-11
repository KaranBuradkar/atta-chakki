package com.atachakki.security.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessTokenDto(@JsonProperty(value = "access_token") String accessToken) {
}
