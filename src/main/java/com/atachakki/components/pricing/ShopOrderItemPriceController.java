package com.atachakki.components.pricing;

import com.atachakki.controller.BaseController;
import com.atachakki.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/shops/{shopId}/prices")
public class ShopOrderItemPriceController extends BaseController {

    private final ShopOrderItemPriceService orderItemPriceService;

    protected ShopOrderItemPriceController(HttpServletRequest request, ShopOrderItemPriceService orderItemPriceService) {
        super(request);
        this.orderItemPriceService = orderItemPriceService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ShopOrderItemPriceResponseDto>>> fetchShopOrderItemPrice(
            @PathVariable Long shopId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sort", defaultValue = "orderItem.name") String sort
    ) {
        Page<ShopOrderItemPriceResponseDto> responseDtoPage = orderItemPriceService
                .getShopOrderItemsPrice(shopId, page, size, direction, sort);
        return apiResponse(HttpStatus.FOUND, "ShopOrderItemPrice fetched successfully", responseDtoPage);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ShopOrderItemPriceResponseDto>> createOrderItemPrice(
            @PathVariable Long shopId,
            @Valid @RequestBody ShopOrderItemPriceRequestDto requestDto
    ) {
        ShopOrderItemPriceResponseDto response = orderItemPriceService.create(shopId, requestDto);
        return apiResponse(HttpStatus.CREATED, "ShopOrderItemPrice created successfully", response);
    }

    @PatchMapping("/{orderItemPriceId}")
    public ResponseEntity<ApiResponse<ShopOrderItemPriceResponseDto>> createOrderItemPrice(
            @PathVariable Long shopId,
            @PathVariable Long orderItemPriceId,
            @RequestBody ShopOrderItemPriceRequestDto updateRequest
    ) {
        ShopOrderItemPriceResponseDto response = orderItemPriceService.update(shopId, orderItemPriceId, updateRequest);
        return apiResponse(HttpStatus.CREATED, "ShopOrderItemPrice updated successfully", response);
    }

    @DeleteMapping("/{orderItemPriceId}")
    public ResponseEntity<ApiResponse<Void>> createOrderItemPrice(
            @PathVariable Long shopId,
            @PathVariable Long orderItemPriceId
    ) {
        orderItemPriceService.delete(shopId, orderItemPriceId);
        return apiResponse(HttpStatus.CREATED, "ShopOrderItemPrice updated successfully", null);
    }

}
