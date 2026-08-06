package com.fluento.features.user.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.auth.security.FluentoUserDetails;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

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
}