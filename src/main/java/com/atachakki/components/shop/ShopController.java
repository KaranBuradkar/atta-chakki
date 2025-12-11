package com.atachakki.components.shop;

import com.atachakki.controller.BaseController;
import com.atachakki.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/shops")
public class ShopController extends BaseController {

    private final ShopService shopService;

    public ShopController(ShopService shopService, HttpServletRequest request) {
        super(request);
        this.shopService = shopService;
    }

    @GetMapping("/u/{userDetailsId}")
    public ResponseEntity<ApiResponse<List<ShopShortResponseDto>>> fetchAllShops(
            @PathVariable("userDetailsId") Long userDetailsId
    ) {
        List<ShopShortResponseDto> allShops = shopService.getAllShops(userDetailsId);
        return ResponseEntity.ok(apiResponse("shops fetched successfully", allShops));
    }

    @GetMapping("/{shopId}")
    public ResponseEntity<ApiResponse<ShopResponseDto>> fetchShopDetails(
            @PathVariable("shopId") Long shopId
    ) {
        ShopResponseDto shopResponse = shopService.getShopDetails(shopId);
        return ResponseEntity.ok(apiResponse("shop details fetched successfully", shopResponse));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ShopResponseDto>> createShop(
            @RequestBody @Valid ShopRequestDto shopRequestDto
    ) {
        ShopResponseDto newShop = shopService.create(shopRequestDto);
        return ResponseEntity.ok(apiResponse("New shop successfully created", newShop));
    }

    @PatchMapping("/{shopId}/status")
    public ResponseEntity<ApiResponse<ShopResponseDto>> updateShop(
            @PathVariable("shopId") Long shopId,
            @RequestParam("status") ShopStatus status
    ) {
        ShopResponseDto responseDto = shopService.updateShopStatus(shopId, status);
        return ResponseEntity.ok(apiResponse("shop status updated", responseDto));
    }

    @PatchMapping("/{shopId}")
    public ResponseEntity<ApiResponse<ShopResponseDto>> updateShop(
            @PathVariable("shopId") Long shopId,
            @RequestBody ShopRequestDto requestDto
    ) {
        ShopResponseDto responseDto = shopService.updateShopFields(shopId, requestDto);
        return ResponseEntity.ok(apiResponse("shop details updated", responseDto));
    }

    @DeleteMapping("/{shopId}")
    public ResponseEntity<ApiResponse<Void>> deleteShop(
            @PathVariable(value = "shopId") Long shopId
    ) {
        shopService.deleteShop(shopId);
        return ResponseEntity.ok(apiResponse("shop deleted successfully", null));
    }
}
