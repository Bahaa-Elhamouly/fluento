package com.fluento.features.quiz.dto;

import java.util.List;
import java.util.UUID;

public record QuizAttemptResultResponse(
        UUID attemptId,
        UUID quizId,
        int score,
        int maxScore,
        int percentage,
        boolean passed,
        int xpEarned,
        List<QuizQuestionFeedbackResponse> feedback
) {
}
