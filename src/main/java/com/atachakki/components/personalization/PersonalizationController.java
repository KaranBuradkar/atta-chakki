package com.atachakki.components.personalization;

import com.atachakki.controller.BaseController;
import com.atachakki.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/personalizations/users")
public class PersonalizationController extends BaseController {

    private final PersonalizationService personalizationService;

    protected PersonalizationController(
            HttpServletRequest request,
            PersonalizationService personalizationService
    ) {
        super(request);
        this.personalizationService = personalizationService;
    }

    @GetMapping("/{userDetailId}")
    public ResponseEntity<ApiResponse<PersonalizationResponseDto>> fetchPersonalization(
            @PathVariable Long userDetailId
    ) {
        PersonalizationResponseDto responseDto = personalizationService.findPersonalization(userDetailId);
        return apiResponse(HttpStatus.OK, "Personalization fetched successfully", responseDto);
    }

    @PatchMapping("/{userDetailId}")
    public ResponseEntity<ApiResponse<PersonalizationResponseDto>> updatePersonalization(
            @PathVariable Long userDetailId,
            @RequestBody PersonalizationRequestDto requestDto
    ) {
        PersonalizationResponseDto responseDto = personalizationService.update(userDetailId, requestDto);
        return apiResponse(HttpStatus.OK, "Personalization updated successfully", responseDto);
    }
}
