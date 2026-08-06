package com.fluento.features.user.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.auth.security.FluentoUserDetails;
import com.fluento.features.user.dto.UpdateUserProfileRequest;
import com.fluento.features.user.dto.UserProfileResponse;
import com.fluento.features.user.service.UserProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> me(
            @AuthenticationPrincipal FluentoUserDetails userDetails
    ) {
        return ApiResponse.success(
                "Current user retrieved successfully",
                Map.of(
                        "id", userDetails.getId(),
                        "email", userDetails.getUsername(),
                        "authorities", userDetails.getAuthorities()
                )
        );
    }

    @GetMapping("/me/profile")
    public ApiResponse<UserProfileResponse> profile(
            @AuthenticationPrincipal FluentoUserDetails userDetails
    ) {
        return ApiResponse.success(
                "Profile retrieved successfully",
                userProfileService.getProfile(userDetails.getId())
        );
    }

    @PatchMapping("/me/profile")
    public ApiResponse<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal FluentoUserDetails userDetails,
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        return ApiResponse.success(
                "Profile updated successfully",
                userProfileService.updateProfile(
                        userDetails.getId(),
                        request
                )
        );
    }
}
