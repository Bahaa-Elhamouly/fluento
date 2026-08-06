package com.fluento.features.grammar.dto;

import java.util.UUID;

public record GrammarPracticeFeedbackResponse(
        UUID exerciseId,
        boolean correct,
        int pointsAwarded,
        int maxPoints,
        String message
) {
}
