package com.atachakki.components.order;

import com.atachakki.components.customer.CustomerResponseDto;
import com.atachakki.components.customer.CustomerService;
import com.atachakki.controller.BaseController;
import com.atachakki.dto.ApiResponse;
import com.atachakki.entity.type.ExportType;
import com.atachakki.services.ExportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/v1/shops/{shopId}/orders")
public class OrderController extends BaseController {

    private final OrderService orderService;
    private final ExportService exportService;
    private final CustomerService customerService;

    protected OrderController(
            HttpServletRequest request,
            OrderService orderService,
            ExportService exportService,
            CustomerService customerService
    ) {
        super(request);
        this.orderService = orderService;
        this.exportService = exportService;
        this.customerService = customerService;
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<ApiResponse<Page<OrderResponseDto>>> fetchOrders(
            @PathVariable(value = "shopId") Long shopId,
            @PathVariable(value = "customerId") Long customerId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "dir", defaultValue = "desc") String direction,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort
    ) {
        Page<OrderResponseDto> orderPage = orderService
                .findOrders(shopId, customerId, page, size, direction, sort);
        return apiResponse(HttpStatus.FOUND, "Orders fetched successfully", orderPage);
    }

    @GetMapping("/total")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotal(@PathVariable Long shopId) {
        BigDecimal totalDebt = orderService.findTotalDebt(shopId);
        return apiResponse(HttpStatus.OK, "Total Debt fetched", totalDebt);
    }

    @PostMapping("/customers/{customerId}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(
            @PathVariable(value = "shopId") Long shopId,
            @PathVariable(value = "customerId") Long customerId,
            @Valid @RequestBody OrderRequestDto requestDto
    ) {
        OrderResponseDto responseDto = orderService.createOrder(shopId, customerId, requestDto);
        return apiResponse(HttpStatus.CREATED, "Order created successfully", responseDto);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> updateOrder(
            @PathVariable(value = "shopId") Long shopId,
            @PathVariable(value = "orderId") Long orderId,
            @RequestBody OrderRequestDto requestDto
    ) {
        OrderResponseDto responseDto = orderService
                .updateOrderFields(shopId, orderId, requestDto);
        return apiResponse(HttpStatus.OK, "Order updated successfully", responseDto);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> deleteOrder(
            @PathVariable(value = "shopId") Long shopId,
            @PathVariable(value = "orderId") Long orderId
    ) {
        orderService.deleteOrder(shopId, orderId);
        return apiResponse(HttpStatus.OK, "Order deleted successfully", null);
    }

    @GetMapping("/customers/{customerId}/export")
    public ResponseEntity<InputStreamResource> exportExcel(
            @PathVariable Long shopId,
            @PathVariable Long customerId,
            @RequestParam("type") ExportType exportType
    ) throws IOException {

        Page<OrderResponseDto> orders = orderService
                .findOrders(shopId, customerId, 0, 100, "asc", "createdAt");
        CustomerResponseDto customer = customerService.findCustomer(shopId, customerId);
        HttpHeaders headers = new HttpHeaders();
        ByteArrayInputStream in = null;

        if (ExportType.CSV.equals(exportType)) {
            in = exportService.exportOrdersToCsv(customer, orders);
            headers.add("Content-Disposition", "attachment; filename=customers.csv");
            headers.add("Content-Type", "text/csv");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(new InputStreamResource(in));
    }
}
