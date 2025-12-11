package com.atachakki.components.orderItem;

import com.atachakki.controller.BaseController;
import com.atachakki.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/order-items")
public class OrderItemController extends BaseController {

    private final OrderItemService orderItemService;

    protected OrderItemController(
            HttpServletRequest request, OrderItemService orderItemService
    ) {
        super(request);
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderItemResponseDto>>> fetchOrderItems(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "sort", defaultValue = "name") String sort
    ) {
        Page<OrderItemResponseDto> responsePage = orderItemService
                .findOrderItems(page, size, direction, sort);
        return apiResponse(HttpStatus.FOUND, "OrderItems fetched successfully", responsePage);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderItemResponseDto>> createOrderItem(
            @Valid @RequestBody OrderItemRequestDto requestDto
    ) {
        OrderItemResponseDto responseDto = orderItemService.create(requestDto);
        return apiResponse(HttpStatus.CREATED, "OrderItem created successfully", responseDto);
    }

    @PatchMapping("/{orderItemId}")
    public ResponseEntity<ApiResponse<OrderItemResponseDto>> updateOrderItem(
            @PathVariable Long orderItemId,
            @RequestBody OrderItemRequestDto requestDto
    ) {
        OrderItemResponseDto responseDto = orderItemService.update(orderItemId, requestDto);
        return apiResponse(HttpStatus.OK, "OrderItem updated successfully", responseDto);
    }

    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<ApiResponse<OrderItemResponseDto>> deleteOrderItem(
            @PathVariable Long orderItemId
    ) {
        orderItemService.deleteById(orderItemId);
        return apiResponse(HttpStatus.OK, "OrderItem deleted successfully", null);
    }
}
