package com.atachakki.components.permissions;

import com.atachakki.controller.BaseController;
import com.atachakki.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = {"http://localhost:5500"})
@RequestMapping("/v1/shops/{shopId}/permissions")
public class StaffPermissionController extends BaseController {

    private final StaffPermissionService staffPermissionService;

    protected StaffPermissionController(
            HttpServletRequest request,
            StaffPermissionService staffPermissionService
    ) {
        super(request);
        this.staffPermissionService = staffPermissionService;
    }

    @GetMapping("/staffs/{staffId}")
    public ResponseEntity<ApiResponse<Page<StaffPermissionResponseDto>>> fetchPermissions(
            @PathVariable Long shopId,
            @PathVariable Long staffId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "sort", defaultValue = "module") String[] sort
    ) {
        Page<StaffPermissionResponseDto> responsePage = staffPermissionService
                .findStaffPermissions(shopId, staffId, page, size, direction, sort);
        return apiResponse(HttpStatus.FOUND, "permissions fetched successfully", responsePage);
    }

    @PostMapping("/staffs/{staffId}")
    public ResponseEntity<ApiResponse<List<StaffPermissionResponseDto>>> createPermissions(
            @PathVariable(value = "shopId") Long shopId,
            @PathVariable(value = "staffId") Long staffId,
            @RequestBody List<@Valid StaffPermissionRequestDto> requestDto
    ) {
        List<StaffPermissionResponseDto> responseDto =
                staffPermissionService.create(shopId, staffId, requestDto);
        return apiResponse(HttpStatus.CREATED, "Permission granted successfully", responseDto);
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<ApiResponse<Void>> deletePermissions(
            @PathVariable(value = "shopId") Long shopId,
            @PathVariable(value = "permissionId") Long permissionId
    ) {
        staffPermissionService.delete(shopId, permissionId);
        return apiResponse(HttpStatus.CREATED, "Permission deleted successfully", null);
    }
}
