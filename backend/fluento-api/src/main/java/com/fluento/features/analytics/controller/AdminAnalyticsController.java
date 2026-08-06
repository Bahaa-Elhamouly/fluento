package com.fluento.features.analytics.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.analytics.dto.AdminAnalyticsResponse;
import com.fluento.features.analytics.service.AdminAnalyticsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/analytics")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminAnalyticsController {

    private final AdminAnalyticsService adminAnalyticsService;

    @GetMapping("/overview")
    public ApiResponse<AdminAnalyticsResponse> overview() {
        return ApiResponse.success(
                "Admin analytics overview retrieved successfully",
                adminAnalyticsService.getOverview()
        );
    }
}
