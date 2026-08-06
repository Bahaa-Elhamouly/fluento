package com.fluento.features.grammar.repository;

import com.fluento.features.course.enums.LearningLevel;
import com.fluento.features.grammar.entity.GrammarRule;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GrammarRuleRepository
        extends JpaRepository<GrammarRule, UUID> {

    @EntityGraph(attributePaths = "language")
    List<GrammarRule> findByLanguage_CodeAndEnabledTrueAndDeletedAtIsNullOrderByPositionAscTitleAsc(
            String languageCode
    );

    @EntityGraph(attributePaths = "language")
    List<GrammarRule> findByLanguage_CodeAndLevelAndEnabledTrueAndDeletedAtIsNullOrderByPositionAscTitleAsc(
            String languageCode,
            LearningLevel level
    );

    @EntityGraph(attributePaths = "language")
    Optional<GrammarRule> findBySlugAndEnabledTrueAndDeletedAtIsNull(
            String slug
    );

    @EntityGraph(attributePaths = "language")
    List<GrammarRule> findByDeletedAtIsNullOrderByPositionAscTitleAsc();

    @EntityGraph(attributePaths = "language")
    Optional<GrammarRule> findByIdAndDeletedAtIsNull(
            UUID id
    );

    boolean existsBySlugAndDeletedAtIsNull(String slug);

    long countByDeletedAtIsNull();
}
