package com.fluento.features.vocabulary.repository;

import com.fluento.features.vocabulary.entity.VocabularyTerm;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VocabularyTermRepository
        extends JpaRepository<VocabularyTerm, UUID> {

    @EntityGraph(attributePaths = "language")
    List<VocabularyTerm> findByLanguage_CodeAndEnabledTrueAndDeletedAtIsNullOrderByTermAsc(
            String languageCode
    );

    @EntityGraph(attributePaths = "language")
    List<VocabularyTerm> findByLanguage_CodeAndTermContainingIgnoreCaseAndEnabledTrueAndDeletedAtIsNullOrderByTermAsc(
            String languageCode,
            String term
    );

    @EntityGraph(attributePaths = "language")
    Optional<VocabularyTerm> findByIdAndEnabledTrueAndDeletedAtIsNull(
            UUID id
    );

    @EntityGraph(attributePaths = "language")
    List<VocabularyTerm> findByDeletedAtIsNullOrderByTermAsc();

    @EntityGraph(attributePaths = "language")
    Optional<VocabularyTerm> findByIdAndDeletedAtIsNull(
            UUID id
    );

    long countByDeletedAtIsNull();
}
