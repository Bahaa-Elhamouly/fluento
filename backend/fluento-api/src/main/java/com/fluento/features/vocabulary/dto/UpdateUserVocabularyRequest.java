package com.fluento.features.vocabulary.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record UpdateUserVocabularyRequest(
        Boolean favorite,

        @Min(0)
        @Max(5)
        Integer masteryLevel,

        Boolean markReviewed
) {
}
