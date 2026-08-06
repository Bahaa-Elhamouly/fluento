package com.fluento.features.vocabulary.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.course.repository.LanguageRepository;
import com.fluento.features.vocabulary.dto.VocabularyTermResponse;
import com.fluento.features.vocabulary.entity.VocabularyTerm;
import com.fluento.features.vocabulary.repository.VocabularyTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VocabularyCatalogService {

    private final LanguageRepository languageRepository;
    private final VocabularyTermRepository vocabularyTermRepository;

    @Transactional(readOnly = true)
    public List<VocabularyTermResponse> getTerms(
            String languageCode,
            String search
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

        List<VocabularyTerm> terms;
        if (search == null || search.isBlank()) {
            terms = vocabularyTermRepository
                    .findByLanguage_CodeAndEnabledTrueAndDeletedAtIsNullOrderByTermAsc(
                            normalizedLanguage
                    );
        } else {
            terms = vocabularyTermRepository
                    .findByLanguage_CodeAndTermContainingIgnoreCaseAndEnabledTrueAndDeletedAtIsNullOrderByTermAsc(
                            normalizedLanguage,
                            search.trim()
                    );
        }

        return terms.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VocabularyTermResponse getTerm(UUID termId) {
        VocabularyTerm term = vocabularyTermRepository
                .findByIdAndEnabledTrueAndDeletedAtIsNull(termId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Vocabulary term",
                                termId
                        )
                );

        return toResponse(term);
    }

    private VocabularyTermResponse toResponse(VocabularyTerm term) {
        return new VocabularyTermResponse(
                term.getId(),
                term.getLanguage().getCode(),
                term.getTerm(),
                term.getTranslation(),
                term.getPronunciation(),
                term.getExample(),
                parseTags(term.getTags())
        );
    }

    private List<String> parseTags(String tags) {
        if (tags == null || tags.isBlank()) {
            return List.of();
        }

        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(tag -> !tag.isBlank())
                .toList();
    }

    private String normalizeLanguage(String languageCode) {
        if (languageCode == null || languageCode.isBlank()) {
            throw new ResourceNotFoundException(
                    "Language",
                    "blank"
            );
        }

        return languageCode.trim().toLowerCase();
    }
}
