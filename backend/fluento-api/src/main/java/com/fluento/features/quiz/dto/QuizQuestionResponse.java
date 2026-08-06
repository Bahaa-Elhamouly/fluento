package com.fluento.features.quiz.dto;

import com.fluento.features.quiz.enums.QuizQuestionType;

import java.util.List;
import java.util.UUID;

public record QuizQuestionResponse(
        UUID id,
        QuizQuestionType type,
        String prompt,
        List<String> options,
        int points,
        int position
) {
}
