package com.atachakki.components.user.details;

import com.atachakki.controller.BaseController;
import com.atachakki.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserDetailsController extends BaseController {

    private final UserInfoDetailsService userInfoDetailsService;

    protected UserDetailsController(
            HttpServletRequest request,
            UserInfoDetailsService userInfoDetailsService
    ) {
        super(request);
        this.userInfoDetailsService = userInfoDetailsService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserInfoUpdate>>> fetchUsers(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "sort", defaultValue = "name") String sort
    ) {
        Page<UserInfoUpdate> response = userInfoDetailsService.findUserInfos(page, size, direction, sort);
        return apiResponse(HttpStatus.FOUND, "UserDetails fetched successfully", response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDetailResponseDto>> fetchUserInfo() {
        UserDetailResponseDto responseDto = userInfoDetailsService.findUserInfos();
        return apiResponse(HttpStatus.FOUND,
                "UserDetails fetched successfully", responseDto);
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserDetailResponseDto>> updateUserInfo(
            @RequestBody UserInfoUpdate updateRequest
    ) {
        UserDetailResponseDto responseDto = userInfoDetailsService.update(updateRequest);
        return apiResponse(HttpStatus.OK,
                "UserDetails fetched successfully", responseDto);
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteUserInfo() {
        userInfoDetailsService.delete();
        return apiResponse(HttpStatus.OK,
                "UserDetails deleted successfully", null);
    }
}
