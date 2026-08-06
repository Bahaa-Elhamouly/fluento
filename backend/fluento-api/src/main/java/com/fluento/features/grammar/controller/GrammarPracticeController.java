package com.fluento.features.grammar.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.auth.security.FluentoUserDetails;
import com.fluento.features.grammar.dto.GrammarExerciseResponse;
import com.fluento.features.grammar.dto.GrammarPracticeResultResponse;
import com.fluento.features.grammar.dto.SubmitGrammarPracticeRequest;
import com.fluento.features.grammar.service.GrammarPracticeService;
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
@RequestMapping("/api/v1/grammar")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class GrammarPracticeController {

    private final GrammarPracticeService grammarPracticeService;

    @GetMapping("/{ruleId}/exercises")
    public ApiResponse<List<GrammarExerciseResponse>> exercises(
            @PathVariable UUID ruleId
    ) {
        return ApiResponse.success(
                "Grammar exercises retrieved successfully",
                grammarPracticeService.getExercises(ruleId)
        );
    }

    @PostMapping("/{ruleId}/practice-attempts")
    public ApiResponse<GrammarPracticeResultResponse> submitAttempt(
            @AuthenticationPrincipal FluentoUserDetails userDetails,
            @PathVariable UUID ruleId,
            @Valid @RequestBody SubmitGrammarPracticeRequest request
    ) {
        return ApiResponse.success(
                "Grammar practice submitted successfully",
                grammarPracticeService.submitAttempt(
                        userDetails.getId(),
                        ruleId,
                        request
                )
        );
    }
}
