package com.fluento.features.quiz.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.auth.security.FluentoUserDetails;
import com.fluento.features.quiz.dto.QuizAttemptResultResponse;
import com.fluento.features.quiz.dto.SubmitQuizAttemptRequest;
import com.fluento.features.quiz.service.QuizAttemptService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/quizzes")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class QuizAttemptController {

    private final QuizAttemptService quizAttemptService;

    @PostMapping("/{quizId}/attempts")
    public ApiResponse<QuizAttemptResultResponse> submitAttempt(
            @AuthenticationPrincipal FluentoUserDetails userDetails,
            @PathVariable UUID quizId,
            @Valid @RequestBody SubmitQuizAttemptRequest request
    ) {
        return ApiResponse.success(
                "Quiz attempt submitted successfully",
                quizAttemptService.submitAttempt(
                        userDetails.getId(),
                        quizId,
                        request
                )
        );
    }
}
