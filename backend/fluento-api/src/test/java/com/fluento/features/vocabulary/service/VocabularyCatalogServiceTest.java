package com.fluento.features.vocabulary.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.course.entity.Language;
import com.fluento.features.course.repository.LanguageRepository;
import com.fluento.features.vocabulary.dto.VocabularyTermResponse;
import com.fluento.features.vocabulary.entity.VocabularyTerm;
import com.fluento.features.vocabulary.repository.VocabularyTermRepository;
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
class VocabularyCatalogServiceTest {

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private VocabularyTermRepository vocabularyTermRepository;

    @InjectMocks
    private VocabularyCatalogService vocabularyCatalogService;

    @Test
    void getTermsReturnsTermsForNormalizedLanguage() {
        Language german = language("de");
        VocabularyTerm hallo = term(german, "Hallo", "greetings, beginner");

        when(languageRepository.findByCodeAndDeletedAtIsNull("de"))
                .thenReturn(Optional.of(german));
        when(vocabularyTermRepository
                .findByLanguage_CodeAndEnabledTrueAndDeletedAtIsNullOrderByTermAsc(
                        "de"
                ))
                .thenReturn(List.of(hallo));

        List<VocabularyTermResponse> response =
                vocabularyCatalogService.getTerms(" DE ", null);

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().languageCode()).isEqualTo("de");
        assertThat(response.getFirst().term()).isEqualTo("Hallo");
        assertThat(response.getFirst().tags())
                .containsExactly("greetings", "beginner");
    }

    @Test
    void getTermsUsesSearchWhenProvided() {
        Language spanish = language("es");
        when(languageRepository.findByCodeAndDeletedAtIsNull("es"))
                .thenReturn(Optional.of(spanish));
        when(vocabularyTermRepository
                .findByLanguage_CodeAndTermContainingIgnoreCaseAndEnabledTrueAndDeletedAtIsNullOrderByTermAsc(
                        "es",
                        "hol"
                ))
                .thenReturn(List.of());

        vocabularyCatalogService.getTerms("es", " hol ");

        verify(vocabularyTermRepository)
                .findByLanguage_CodeAndTermContainingIgnoreCaseAndEnabledTrueAndDeletedAtIsNullOrderByTermAsc(
                        "es",
                        "hol"
                );
    }

    @Test
    void getTermsThrowsWhenLanguageDoesNotExist() {
        when(languageRepository.findByCodeAndDeletedAtIsNull("it"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> vocabularyCatalogService.getTerms("it", null)
        )
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getTermReturnsTermDetail() {
        VocabularyTerm term = term(language("fr"), "Bonjour", "greetings");
        when(vocabularyTermRepository
                .findByIdAndEnabledTrueAndDeletedAtIsNull(term.getId()))
                .thenReturn(Optional.of(term));

        VocabularyTermResponse response =
                vocabularyCatalogService.getTerm(term.getId());

        assertThat(response.term()).isEqualTo("Bonjour");
        assertThat(response.translation()).isEqualTo("Hello");
        assertThat(response.tags()).containsExactly("greetings");
    }

    @Test
    void getTermThrowsWhenMissing() {
        UUID termId = UUID.randomUUID();
        when(vocabularyTermRepository
                .findByIdAndEnabledTrueAndDeletedAtIsNull(termId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> vocabularyCatalogService.getTerm(termId))
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

    private VocabularyTerm term(
            Language language,
            String value,
            String tags
    ) {
        VocabularyTerm term = new VocabularyTerm();
        term.setId(UUID.randomUUID());
        term.setLanguage(language);
        term.setTerm(value);
        term.setTranslation("Hello");
        term.setPronunciation("pronunciation");
        term.setExample("Example sentence");
        term.setTags(tags);
        term.setEnabled(true);
        return term;
    }
}
