package com.fluento.common.util;

public final class TextNormalizer {

    private TextNormalizer() {
    }

    public static String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    public static String lowerTrim(String value) {
        return value == null ? null : value.trim().toLowerCase();
    }
}
