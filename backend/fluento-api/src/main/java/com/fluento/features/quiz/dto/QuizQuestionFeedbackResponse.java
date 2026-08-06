package com.fluento.features.quiz.dto;

import java.util.UUID;

public record QuizQuestionFeedbackResponse(
        UUID questionId,
        boolean correct,
        int pointsAwarded,
        int maxPoints,
        String message
) {
}
