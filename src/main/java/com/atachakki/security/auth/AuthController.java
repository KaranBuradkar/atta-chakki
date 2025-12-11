package com.atachakki.security.auth;

import com.atachakki.controller.BaseController;
import com.atachakki.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(value = "http://localhost:5500")
public class AuthController extends BaseController {

    private final AuthService authService;

    protected AuthController(HttpServletRequest request, AuthService authService) {
        super(request);
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponseDto>> register(
            @RequestBody @Valid RegisterRequestDto requestDto
    ) {
        RegisterResponseDto responseDto = authService.register(requestDto);
        return ResponseEntity.ok(apiResponse("New account created successfully", responseDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<RegisterResponseDto>> login(
            @RequestBody @Valid LoginRequestDto requestDto
    ) {
        RegisterResponseDto responseDto = authService.login(requestDto);
        return ResponseEntity.ok(apiResponse("Login successfully", responseDto));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AccessTokenDto>> refreshToken() {
        AccessTokenDto accessTokenDto = authService.refreshToken(request);
        return ResponseEntity.ok(apiResponse("Access token successfully refresh", accessTokenDto));
    }
}
