package com.atachakki.components.staff;

import com.atachakki.controller.BaseController;
import com.atachakki.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/shops/{shopId}/staffs")
public class ShopStaffController extends BaseController {

    private final ShopStaffService shopStaffService;

    protected ShopStaffController(HttpServletRequest request, ShopStaffService shopStaffService) {
        super(request);
        this.shopStaffService = shopStaffService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ShopStaffResponseDto>>> fetchShopStaffs(
            @PathVariable Long shopId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort
    ) {
        Page<ShopStaffResponseDto> responsePage = shopStaffService
                .findShopStaffs(shopId, page, size, direction, sort);
        return apiResponse(HttpStatus.FOUND, "ShopStaff fetched successfully", responsePage);
    }

    @GetMapping("/{shopStaffId}")
    public ResponseEntity<ApiResponse<ShopStaffResponseDto>> fetchShopStaff(
            @PathVariable Long shopId,
            @PathVariable Long shopStaffId
    ) {
        ShopStaffResponseDto responseDto = shopStaffService.findShopStaff(shopId, shopStaffId);
        return apiResponse(HttpStatus.OK, "shop staff updated successfully", responseDto);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ShopStaffResponseDto>> createShopStaff(
            @PathVariable Long shopId,
            @Valid @RequestBody ShopStaffRequestDto requestDto
    ) {
        ShopStaffResponseDto responseDto = shopStaffService.create(shopId, requestDto);
        return apiResponse(HttpStatus.CREATED, "New shop staff register successfully", responseDto);
    }

    @PatchMapping("/{shopStaffId}")
    public ResponseEntity<ApiResponse<ShopStaffResponseDto>> updateShopStaff(
            @PathVariable Long shopId,
            @PathVariable Long shopStaffId,
            @RequestBody ShopStaffRequestDto requestDto
    ) {
        ShopStaffResponseDto responseDto = shopStaffService.update(shopId, shopStaffId, requestDto);
        return apiResponse(HttpStatus.OK, "shop staff updated successfully", responseDto);
    }

    @DeleteMapping("/{shopStaffId}")
    public ResponseEntity<ApiResponse<ShopStaffResponseDto>> deleteShopStaff(
            @PathVariable Long shopId,
            @PathVariable Long shopStaffId
    ) {
        shopStaffService.deleteById(shopId, shopStaffId);
        return apiResponse(HttpStatus.OK, "shop staff deleted successfully", null);
    }
}
