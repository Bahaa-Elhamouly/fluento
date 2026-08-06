package com.fluento.features.course.dto;

import java.util.UUID;

public record LanguageResponse(
        UUID id,
        String code,
        String name,
        String nativeName
) {
}
