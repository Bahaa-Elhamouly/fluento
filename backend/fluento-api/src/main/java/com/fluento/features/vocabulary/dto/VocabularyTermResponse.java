package com.fluento.features.vocabulary.dto;

import java.util.List;
import java.util.UUID;

public record VocabularyTermResponse(
        UUID id,
        String languageCode,
        String term,
        String translation,
        String pronunciation,
        String example,
        List<String> tags
) {
}
