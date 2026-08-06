package com.fluento.features.grammar.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.course.enums.LearningLevel;
import com.fluento.features.grammar.dto.GrammarRuleDetailResponse;
import com.fluento.features.grammar.dto.GrammarRuleSummaryResponse;
import com.fluento.features.grammar.service.GrammarCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/grammar")
@RequiredArgsConstructor
public class GrammarCatalogController {

    private final GrammarCatalogService grammarCatalogService;

    @GetMapping
    public ApiResponse<List<GrammarRuleSummaryResponse>> rules(
            @RequestParam String language,
            @RequestParam(required = false) LearningLevel level
    ) {
        return ApiResponse.success(
                "Grammar rules retrieved successfully",
                grammarCatalogService.getRules(language, level)
        );
    }

    @GetMapping("/{slug}")
    public ApiResponse<GrammarRuleDetailResponse> rule(
            @PathVariable String slug
    ) {
        return ApiResponse.success(
                "Grammar rule retrieved successfully",
                grammarCatalogService.getRule(slug)
        );
    }
}
