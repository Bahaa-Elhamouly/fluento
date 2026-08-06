package com.fluento.features.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AdminVocabularyTermRequest(
        @NotBlank
        @Size(max = 12)
        String languageCode,

        @NotBlank
        @Size(max = 160)
        String term,

        @NotBlank
        @Size(max = 160)
        String translation,

        @Size(max = 160)
        String pronunciation,

        @Size(max = 1000)
        String example,

        @NotEmpty
        List<@NotBlank @Size(max = 60) String> tags,

        boolean enabled
) {
}
