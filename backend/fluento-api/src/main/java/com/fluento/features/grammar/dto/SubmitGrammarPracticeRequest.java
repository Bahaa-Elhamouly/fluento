package com.fluento.features.grammar.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SubmitGrammarPracticeRequest(
        @NotEmpty
        List<@Valid GrammarPracticeAnswerRequest> answers
) {
}
