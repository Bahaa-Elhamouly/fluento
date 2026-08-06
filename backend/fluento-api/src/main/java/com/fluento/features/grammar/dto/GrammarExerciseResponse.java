package com.fluento.features.grammar.dto;

import com.fluento.features.course.enums.LearningLevel;
import com.fluento.features.grammar.enums.GrammarExerciseType;

import java.util.List;
import java.util.UUID;

public record GrammarExerciseResponse(
        UUID id,
        UUID ruleId,
        GrammarExerciseType type,
        String prompt,
        List<String> options,
        String explanation,
        LearningLevel level,
        int points,
        int position
) {
}
