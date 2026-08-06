package com.fluento.features.progress.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.auth.security.FluentoUserDetails;
import com.fluento.features.progress.dto.CompleteLessonRequest;
import com.fluento.features.progress.dto.LessonProgressResponse;
import com.fluento.features.progress.dto.ProgressSummaryResponse;
import com.fluento.features.progress.service.ProgressService;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @PostMapping("/lessons/{lessonId}/complete")
    public ApiResponse<LessonProgressResponse> completeLesson(
            @AuthenticationPrincipal FluentoUserDetails userDetails,
            @PathVariable UUID lessonId,
            @Valid @RequestBody(required = false) CompleteLessonRequest request
    ) {
        return ApiResponse.success(
                "Lesson completed successfully",
                progressService.completeLesson(
                        userDetails.getId(),
                        lessonId,
                        request
                )
        );
    }

    @GetMapping("/progress/me")
    public ApiResponse<ProgressSummaryResponse> myProgress(
            @AuthenticationPrincipal FluentoUserDetails userDetails
    ) {
        return ApiResponse.success(
                "Progress retrieved successfully",
                progressService.getSummary(userDetails.getId())
        );
    }
}
