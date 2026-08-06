package com.fluento.features.grammar.dto;

import com.fluento.features.course.enums.LearningLevel;

import java.util.UUID;

public record GrammarRuleSummaryResponse(
        UUID id,
        String languageCode,
        String title,
        String slug,
        LearningLevel level,
        int position
) {
}
