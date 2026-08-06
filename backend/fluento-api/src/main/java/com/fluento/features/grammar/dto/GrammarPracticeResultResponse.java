package com.fluento.features.grammar.dto;

import java.util.List;
import java.util.UUID;

public record GrammarPracticeResultResponse(
        UUID attemptId,
        UUID ruleId,
        int score,
        int maxScore,
        int percentage,
        int xpEarned,
        List<GrammarPracticeFeedbackResponse> feedback
) {
}
