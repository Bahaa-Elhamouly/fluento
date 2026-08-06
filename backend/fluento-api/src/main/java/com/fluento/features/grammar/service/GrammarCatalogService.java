package com.fluento.features.grammar.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.course.enums.LearningLevel;
import com.fluento.features.course.repository.LanguageRepository;
import com.fluento.features.grammar.dto.GrammarExampleResponse;
import com.fluento.features.grammar.dto.GrammarRuleDetailResponse;
import com.fluento.features.grammar.dto.GrammarRuleSummaryResponse;
import com.fluento.features.grammar.entity.GrammarExample;
import com.fluento.features.grammar.entity.GrammarRule;
import com.fluento.features.grammar.repository.GrammarExampleRepository;
import com.fluento.features.grammar.repository.GrammarRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GrammarCatalogService {

    private final LanguageRepository languageRepository;
    private final GrammarRuleRepository grammarRuleRepository;
    private final GrammarExampleRepository grammarExampleRepository;

    @Transactional(readOnly = true)
    public List<GrammarRuleSummaryResponse> getRules(
            String languageCode,
            LearningLevel level
    ) {
        String normalizedLanguage = normalizeLanguage(languageCode);

        if (languageRepository
                .findByCodeAndDeletedAtIsNull(normalizedLanguage)
                .isEmpty()) {
            throw new ResourceNotFoundException(
                    "Language",
                    normalizedLanguage
            );
        }

        List<GrammarRule> rules;
        if (level == null) {
            rules = grammarRuleRepository
                    .findByLanguage_CodeAndEnabledTrueAndDeletedAtIsNullOrderByPositionAscTitleAsc(
                            normalizedLanguage
                    );
        } else {
            rules = grammarRuleRepository
                    .findByLanguage_CodeAndLevelAndEnabledTrueAndDeletedAtIsNullOrderByPositionAscTitleAsc(
                            normalizedLanguage,
                            level
                    );
        }

        return rules.stream()
                .map(this::toSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public GrammarRuleDetailResponse getRule(String slug) {
        GrammarRule rule = grammarRuleRepository
                .findBySlugAndEnabledTrueAndDeletedAtIsNull(slug)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Grammar rule",
                                slug
                        )
                );

        List<GrammarExampleResponse> examples =
                grammarExampleRepository
                        .findByRule_IdAndDeletedAtIsNullOrderByPositionAsc(
                                rule.getId()
                        )
                        .stream()
                        .map(this::toExampleResponse)
                        .toList();

        return new GrammarRuleDetailResponse(
                rule.getId(),
                rule.getLanguage().getCode(),
                rule.getTitle(),
                rule.getSlug(),
                rule.getExplanation(),
                rule.getLevel(),
                rule.getPosition(),
                examples
        );
    }

    private GrammarRuleSummaryResponse toSummaryResponse(
            GrammarRule rule
    ) {
        return new GrammarRuleSummaryResponse(
                rule.getId(),
                rule.getLanguage().getCode(),
                rule.getTitle(),
                rule.getSlug(),
                rule.getLevel(),
                rule.getPosition()
        );
    }

    private GrammarExampleResponse toExampleResponse(
            GrammarExample example
    ) {
        return new GrammarExampleResponse(
                example.getSourceText(),
                example.getTranslation(),
                example.getNote(),
                example.getPosition()
        );
    }

    private String normalizeLanguage(String languageCode) {
        if (languageCode == null || languageCode.isBlank()) {
            throw new ResourceNotFoundException("Language", "blank");
        }

        return languageCode.trim().toLowerCase();
    }
}
