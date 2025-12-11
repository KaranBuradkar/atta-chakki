package com.atachakki.components.payment;

import com.atachakki.components.order.OrderRequestDto;
import com.atachakki.components.order.OrderResponseDto;
import com.atachakki.controller.BaseController;
import com.atachakki.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/shops/{shopId}/payments")
public class PaymentController extends BaseController {

    private final PaymentService paymentService;

    protected PaymentController(
            HttpServletRequest request,
            PaymentService paymentService
    ) {
        super(request);
        this.paymentService = paymentService;
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<ApiResponse<Page<PaymentResponseShortDto>>> fetchPayments(
            @PathVariable(value = "shopId") Long shopId,
            @PathVariable(value = "customerId") Long customerId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "dir", defaultValue = "desc") String direction,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort
    ) {
        Page<PaymentResponseShortDto> responseDtoPage = paymentService
                .findPayments(shopId, customerId, page, size, direction, sort);
        return apiResponse(HttpStatus.FOUND, "Payments fetched successfully", responseDtoPage);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> fetchPayment(
            @PathVariable(value = "shopId") Long shopId,
            @PathVariable(value = "paymentId") Long paymentId
    ) {
        PaymentResponseDto responseDto = paymentService
                .findPayment(shopId, paymentId);
        return apiResponse(HttpStatus.OK, "Payment fetched successfully", responseDto);
    }

    @PostMapping("/customers/{customerId}")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> createPayment(
            @PathVariable(value = "shopId") Long shopId,
            @PathVariable(value = "customerId") Long customerId,
            @Valid @RequestBody PaymentBundleRequestDto requestDto
    ) {
        PaymentResponseDto responseDto = paymentService.create(shopId, customerId, requestDto);
        return apiResponse(HttpStatus.CREATED, "Payment successful", responseDto);
    }

    @PatchMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> updatePayment(
            @PathVariable(value = "shopId") Long shopId,
            @PathVariable(value = "paymentId") Long paymentId,
            @RequestBody PaymentRequestDto updateRequest
    ) {
        PaymentResponseDto responseDto = paymentService.update(shopId, paymentId, updateRequest);
        return apiResponse(HttpStatus.CREATED, "Payment updated successfully", responseDto);
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<Void>> deletePayment(
            @PathVariable(value = "shopId") Long shopId,
            @PathVariable(value = "paymentId") Long paymentId
    ) {
        paymentService.deleteById(shopId, paymentId);
        return apiResponse(HttpStatus.CREATED, "Payment transaction deleted successfully", null);
    }

}
