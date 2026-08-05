package com.fluento.common.controller;

import com.fluento.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/public")
public class HealthController {

    @GetMapping("/status")
    public ApiResponse<Map<String, String>> status() {
        return ApiResponse.success(
                "Fluento API is running",
                Map.of(
                        "application", "fluento-api",
                        "status", "UP"
                )
        );
    }
}