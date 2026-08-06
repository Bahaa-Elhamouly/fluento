package com.fluento.features.grammar.dto;

import com.fluento.features.course.enums.LearningLevel;

import java.util.List;
import java.util.UUID;

public record GrammarRuleDetailResponse(
        UUID id,
        String languageCode,
        String title,
        String slug,
        String explanation,
        LearningLevel level,
        int position,
        List<GrammarExampleResponse> examples
) {
}
