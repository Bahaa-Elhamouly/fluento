package com.fluento.features.admin.dto;

import com.fluento.features.quiz.enums.QuizQuestionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AdminQuizQuestionRequest(
        @NotNull
        QuizQuestionType type,

        @NotBlank
        @Size(max = 1000)
        String prompt,

        List<@NotBlank @Size(max = 300) String> options,

        @NotBlank
        @Size(max = 500)
        String correctAnswer,

        @Size(max = 1000)
        String explanation,

        @Min(1)
        int points,

        @Min(1)
        int position
) {
}
