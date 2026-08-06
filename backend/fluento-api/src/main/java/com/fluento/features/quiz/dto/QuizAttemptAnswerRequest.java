package com.fluento.features.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record QuizAttemptAnswerRequest(
        @NotNull
        UUID questionId,

        @NotBlank
        @Size(max = 1000)
        String answer
) {
}
