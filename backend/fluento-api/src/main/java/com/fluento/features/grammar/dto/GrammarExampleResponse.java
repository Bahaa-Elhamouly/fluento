package com.fluento.features.grammar.dto;

public record GrammarExampleResponse(
        String sourceText,
        String translation,
        String note,
        int position
) {
}
