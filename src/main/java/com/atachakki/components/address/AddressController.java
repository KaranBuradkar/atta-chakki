package com.atachakki.components.address;

import com.atachakki.controller.BaseController;
import com.atachakki.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/address")
public class AddressController extends BaseController {

    private final AddressService addressService;

    public AddressController(AddressService addressService, HttpServletRequest request) {
        super(request);
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressDto>>> fetchAddresses (
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "by", defaultValue = "asc") String by,
            @RequestParam(name = "sort", defaultValue = "id") String... sort) {
        List<AddressDto> address = addressService.getAddressPage(page, size, sort, by);
        return ResponseEntity.ok(apiResponse("fetched all address", address));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDto>> fetchAddress(@PathVariable("id") Long addressId) {
        AddressDto resAddress = addressService.getAddress(addressId);
        return ResponseEntity.ok(apiResponse("address fetched successfully", resAddress));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressDto>> addAddress(@Valid AddressDto addressDto) {
        AddressDto resAddress = addressService.createAddress(addressDto);
        return ResponseEntity.ok(apiResponse("address added successfully", resAddress));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDto>> fetchAddress(
            @PathVariable("id") Long addressId,
            @RequestBody Map<String, Objects> map
            ) {
        AddressDto resAddress = addressService.updateAddress(addressId, map);
        return ResponseEntity.ok(apiResponse("address updated successfully", resAddress));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDto>> deleteAddress(@PathVariable("id") Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.ok(apiResponse("address deleted successfully", null));
    }

}
