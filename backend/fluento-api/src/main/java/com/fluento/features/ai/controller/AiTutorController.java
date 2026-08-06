package com.fluento.features.ai.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.ai.dto.AiSessionResponse;
import com.fluento.features.ai.dto.CreateAiSessionRequest;
import com.fluento.features.ai.dto.SendAiMessageRequest;
import com.fluento.features.ai.service.AiTutorService;
import com.fluento.features.auth.security.FluentoUserDetails;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ai/tutor")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AiTutorController {

    private final AiTutorService aiTutorService;

    @GetMapping("/sessions")
    public ApiResponse<List<AiSessionResponse>> sessions(
            @AuthenticationPrincipal FluentoUserDetails userDetails
    ) {
        return ApiResponse.success(
                "AI tutor sessions retrieved successfully",
                aiTutorService.getSessions(userDetails.getId())
        );
    }

    @PostMapping("/sessions")
    public ApiResponse<AiSessionResponse> createSession(
            @AuthenticationPrincipal FluentoUserDetails userDetails,
            @Valid @RequestBody CreateAiSessionRequest request
    ) {
        return ApiResponse.success(
                "AI tutor session created successfully",
                aiTutorService.createSession(userDetails.getId(), request)
        );
    }

    @GetMapping("/sessions/{sessionId}")
    public ApiResponse<AiSessionResponse> session(
            @AuthenticationPrincipal FluentoUserDetails userDetails,
            @PathVariable UUID sessionId
    ) {
        return ApiResponse.success(
                "AI tutor session retrieved successfully",
                aiTutorService.getSession(userDetails.getId(), sessionId)
        );
    }

    @PostMapping("/sessions/{sessionId}/messages")
    public ApiResponse<AiSessionResponse> sendMessage(
            @AuthenticationPrincipal FluentoUserDetails userDetails,
            @PathVariable UUID sessionId,
            @Valid @RequestBody SendAiMessageRequest request
    ) {
        return ApiResponse.success(
                "AI tutor message processed successfully",
                aiTutorService.sendMessage(
                        userDetails.getId(),
                        sessionId,
                        request
                )
        );
    }
}
