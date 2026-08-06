package com.fluento.features.grammar.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.course.entity.Language;
import com.fluento.features.course.enums.LearningLevel;
import com.fluento.features.course.repository.LanguageRepository;
import com.fluento.features.grammar.dto.GrammarRuleDetailResponse;
import com.fluento.features.grammar.dto.GrammarRuleSummaryResponse;
import com.fluento.features.grammar.entity.GrammarExample;
import com.fluento.features.grammar.entity.GrammarRule;
import com.fluento.features.grammar.repository.GrammarExampleRepository;
import com.fluento.features.grammar.repository.GrammarRuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrammarCatalogServiceTest {

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private GrammarRuleRepository grammarRuleRepository;

    @Mock
    private GrammarExampleRepository grammarExampleRepository;

    @InjectMocks
    private GrammarCatalogService grammarCatalogService;

    @Test
    void getRulesReturnsRulesForNormalizedLanguage() {
        Language german = language("de");
        GrammarRule rule = rule(german);

        when(languageRepository.findByCodeAndDeletedAtIsNull("de"))
                .thenReturn(Optional.of(german));
        when(grammarRuleRepository
                .findByLanguage_CodeAndEnabledTrueAndDeletedAtIsNullOrderByPositionAscTitleAsc(
                        "de"
                ))
                .thenReturn(List.of(rule));

        List<GrammarRuleSummaryResponse> response =
                grammarCatalogService.getRules(" DE ", null);

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().languageCode()).isEqualTo("de");
        assertThat(response.getFirst().title())
                .isEqualTo("German verb position");
        assertThat(response.getFirst().level())
                .isEqualTo(LearningLevel.BEGINNER);
    }

    @Test
    void getRulesUsesLevelFilterWhenProvided() {
        Language spanish = language("es");
        when(languageRepository.findByCodeAndDeletedAtIsNull("es"))
                .thenReturn(Optional.of(spanish));
        when(grammarRuleRepository
                .findByLanguage_CodeAndLevelAndEnabledTrueAndDeletedAtIsNullOrderByPositionAscTitleAsc(
                        "es",
                        LearningLevel.BEGINNER
                ))
                .thenReturn(List.of());

        grammarCatalogService.getRules("es", LearningLevel.BEGINNER);

        verify(grammarRuleRepository)
                .findByLanguage_CodeAndLevelAndEnabledTrueAndDeletedAtIsNullOrderByPositionAscTitleAsc(
                        "es",
                        LearningLevel.BEGINNER
                );
    }

    @Test
    void getRulesThrowsWhenLanguageDoesNotExist() {
        when(languageRepository.findByCodeAndDeletedAtIsNull("it"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> grammarCatalogService.getRules("it", null)
        )
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getRuleReturnsRuleDetailWithExamples() {
        GrammarRule rule = rule(language("fr"));
        GrammarExample example = example(rule);

        when(grammarRuleRepository
                .findBySlugAndEnabledTrueAndDeletedAtIsNull(rule.getSlug()))
                .thenReturn(Optional.of(rule));
        when(grammarExampleRepository
                .findByRule_IdAndDeletedAtIsNullOrderByPositionAsc(
                        rule.getId()
                ))
                .thenReturn(List.of(example));

        GrammarRuleDetailResponse response =
                grammarCatalogService.getRule(rule.getSlug());

        assertThat(response.id()).isEqualTo(rule.getId());
        assertThat(response.languageCode()).isEqualTo("fr");
        assertThat(response.explanation()).contains("verb");
        assertThat(response.examples()).hasSize(1);
        assertThat(response.examples().getFirst().sourceText())
                .isEqualTo("Ich lerne Deutsch.");
    }

    @Test
    void getRuleThrowsWhenMissing() {
        when(grammarRuleRepository
                .findBySlugAndEnabledTrueAndDeletedAtIsNull("missing-rule"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> grammarCatalogService.getRule("missing-rule"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private Language language(String code) {
        Language language = new Language();
        language.setId(UUID.randomUUID());
        language.setCode(code);
        language.setName("Language");
        language.setNativeName("Language");
        language.setEnabled(true);
        return language;
    }

    private GrammarRule rule(Language language) {
        GrammarRule rule = new GrammarRule();
        rule.setId(UUID.randomUUID());
        rule.setLanguage(language);
        rule.setTitle("German verb position");
        rule.setSlug("german-verb-position");
        rule.setExplanation("Main clauses usually keep the verb in position two.");
        rule.setLevel(LearningLevel.BEGINNER);
        rule.setEnabled(true);
        rule.setPosition(1);
        return rule;
    }

    private GrammarExample example(GrammarRule rule) {
        GrammarExample example = new GrammarExample();
        example.setId(UUID.randomUUID());
        example.setRule(rule);
        example.setSourceText("Ich lerne Deutsch.");
        example.setTranslation("I am learning German.");
        example.setNote("The conjugated verb is second.");
        example.setPosition(1);
        return example;
    }
}
