package com.atachakki.security.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    RegisterResponseDto register(@Valid RegisterRequestDto requestDto);

    RegisterResponseDto login(@Valid LoginRequestDto requestDto);

    AccessTokenDto refreshToken(HttpServletRequest request);
}
