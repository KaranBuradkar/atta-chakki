package com.atachakki.components.operation;

import com.atachakki.controller.BaseController;
import com.atachakki.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/shops/{shopId}/notifications")
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
                shopOperationService.findNotifications(shopId, page, size, direction, sort);
        return apiResponse(HttpStatus.OK, "Notifications fetched successfully", responseDto);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @PathVariable Long shopId,
            @PathVariable Long notificationId
    ) {
        shopOperationService.delete(shopId, notificationId);
        return apiResponse(HttpStatus.OK, "Notification deleted successfully", null);
    }
}
