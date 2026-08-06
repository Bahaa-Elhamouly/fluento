package com.fluento.features.admin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdminGrammarExampleRequest(
        @NotBlank
        @Size(max = 500)
        String sourceText,

        @NotBlank
        @Size(max = 500)
        String translation,

        @Size(max = 500)
        String note,

        @Min(1)
        int position
) {
}
