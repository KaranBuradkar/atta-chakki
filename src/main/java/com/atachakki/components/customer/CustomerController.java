package com.atachakki.components.customer;

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

@RestController
@RequestMapping("/v1/shops/{shopId}/customers")
public class CustomerController extends BaseController {

    private final CustomerService customerService;
    private final ExportService exportService;

    protected CustomerController(
            HttpServletRequest request,
            CustomerService customerService,
            ExportService exportService
    ) {
        super(request);
        this.customerService = customerService;
        this.exportService = exportService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CustomerResponseShortDto>>> fetchCustomers(
            @PathVariable(value = "shopId") Long shopId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "dir", defaultValue = "asc") String direction,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(required = false) String name
    ) {
        Page<CustomerResponseShortDto> response = customerService
                .findCustomers(shopId, page, size, direction, sort, name);
        return ResponseEntity.ok(apiResponse("Customers fetched successfully", response));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerResponseDto>> fetchCustomer(
            @PathVariable(value = "shopId") Long shopId,
            @PathVariable(value = "customerId") Long customerId
    ) {
        CustomerResponseDto responseDto = customerService.findCustomer(shopId, customerId);
        return ResponseEntity.ok(apiResponse("customer fetched successfully", responseDto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponseDto>> createCustomer(
            @PathVariable("shopId") Long shopId,
            @Valid @RequestBody CustomerRequestDto requestDto
    ) {
        CustomerResponseDto response = customerService.create(shopId, requestDto);
        return ResponseEntity.ok(apiResponse("New customer register successfully", response));
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerResponseDto>> blockCustomer(
            @PathVariable(value = "shopId") Long shopId,
            @PathVariable(value = "customerId") Long customerId,
            @RequestBody CustomerRequestDto requestDto
    ) {
        CustomerResponseDto responseDto = customerService
                .updateCustomerFields(shopId, customerId, requestDto);
        return ResponseEntity.ok(apiResponse("Customer updated successfully", responseDto));
    }

    @PatchMapping("/{customerId}/block")
    public ResponseEntity<ApiResponse<CustomerResponseDto>> blockCustomer(
            @PathVariable(value = "shopId") Long shopId,
            @PathVariable(value = "customerId") Long customerId,
            @RequestParam(value = "blocked") Boolean blocked
    ) {
        CustomerResponseDto responseDto = customerService
                .updateCustomerBlockStatus(shopId, customerId, blocked);
        return ResponseEntity.ok(apiResponse(blocked ? "Customer blocked" : "Customer unblocked", responseDto));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerResponseDto>> deleteCustomer(
            @PathVariable(value = "shopId") Long shopId,
            @PathVariable(value = "customerId") Long customerId
    ) {
        customerService.deleteById(shopId, customerId);
        return ResponseEntity.ok(apiResponse("Customer deleted successfully", null));
    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportExcel(
            @PathVariable Long shopId,
            @RequestParam("type") ExportType exportType
    ) throws IOException {

        Page<CustomerResponseDto> customers = customerService
                .findAllCustomers(shopId, 0, 200, "asc", "name");
        HttpHeaders headers = new HttpHeaders();
        ByteArrayInputStream in = null;

       if (ExportType.CSV.equals(exportType)) {
           in = exportService.exportCustomersToCsv(customers);
           headers.add("Content-Disposition", "attachment; filename=customers.csv");
           headers.add("Content-Type", "text/csv");
       }

       if (ExportType.PDF.equals(exportType)) {
           in = exportService.exportCustomersToPdf(customers);
       }

       return ResponseEntity.status(HttpStatus.OK)
               .headers(headers)
               .body(new InputStreamResource(in));
    }
}
