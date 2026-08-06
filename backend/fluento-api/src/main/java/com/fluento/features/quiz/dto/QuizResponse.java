package com.fluento.features.quiz.dto;

import java.util.List;
import java.util.UUID;

public record QuizResponse(
        UUID id,
        UUID lessonId,
        String title,
        String description,
        int passingScore,
        int position,
        List<QuizQuestionResponse> questions
) {
}
