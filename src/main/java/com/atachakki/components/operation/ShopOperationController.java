package com.atachakki.components.operation;

import com.atachakki.controller.BaseController;
import com.atachakki.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/shops/{shopId}/shop-operations")
public class ShopOperationController extends BaseController {

    private final ShopOperationService shopOperationService;

    protected ShopOperationController(
            HttpServletRequest request,
            ShopOperationService shopOperationService
    ) {
        super(request);
        this.shopOperationService = shopOperationService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ShopOperationResponseDto>>> fetchNotifications(
            @PathVariable(value = "shopId") Long shopId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "dir", defaultValue = "desc") String direction,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort
    ) {
        Page<ShopOperationResponseDto> responseDto =
                shopOperationService.findShopOperation(shopId, page, size, direction, sort);
        return apiResponse(HttpStatus.OK, "Shop operations fetched successfully", responseDto);
    }

    @DeleteMapping("/{operationId}")
    public ResponseEntity<ApiResponse<Void>> deleteOperationId(
            @PathVariable Long shopId,
            @PathVariable Long operationId
    ) {
        shopOperationService.delete(shopId, operationId);
        return apiResponse(HttpStatus.OK, "Shop operation deleted successfully", null);
    }
}
