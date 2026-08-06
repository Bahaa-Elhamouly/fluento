package com.fluento.features.vocabulary.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.vocabulary.dto.VocabularyTermResponse;
import com.fluento.features.vocabulary.service.VocabularyCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/catalog/vocabulary")
@RequiredArgsConstructor
public class VocabularyCatalogController {

    private final VocabularyCatalogService vocabularyCatalogService;

    @GetMapping
    public ApiResponse<List<VocabularyTermResponse>> terms(
            @RequestParam String language,
            @RequestParam(required = false) String search
    ) {
        return ApiResponse.success(
                "Vocabulary terms retrieved successfully",
                vocabularyCatalogService.getTerms(language, search)
        );
    }

    @GetMapping("/{termId}")
    public ApiResponse<VocabularyTermResponse> term(
            @PathVariable UUID termId
    ) {
        return ApiResponse.success(
                "Vocabulary term retrieved successfully",
                vocabularyCatalogService.getTerm(termId)
        );
    }
}
