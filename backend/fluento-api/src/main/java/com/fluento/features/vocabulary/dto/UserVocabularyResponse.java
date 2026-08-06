package com.fluento.features.vocabulary.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserVocabularyResponse(
        UUID termId,
        String languageCode,
        String term,
        String translation,
        boolean favorite,
        int masteryLevel,
        LocalDateTime lastReviewedAt
) {
}
