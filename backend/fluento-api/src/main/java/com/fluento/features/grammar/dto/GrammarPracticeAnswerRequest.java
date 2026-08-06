package com.fluento.features.grammar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GrammarPracticeAnswerRequest(
        @NotNull
        UUID exerciseId,

        @NotBlank
        String answer
) {
}
