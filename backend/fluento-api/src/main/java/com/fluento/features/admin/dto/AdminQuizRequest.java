package com.fluento.features.admin.dto;

import com.fluento.features.quiz.enums.QuizStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AdminQuizRequest(
        @NotBlank
        @Size(max = 180)
        String title,

        @Size(max = 1000)
        String description,

        @NotNull
        QuizStatus status,

        @Min(1)
        @Max(100)
        int passingScore,

        @Min(1)
        int position,

        List<@Valid AdminQuizQuestionRequest> questions
) {
}
