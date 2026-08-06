package com.fluento.features.grammar.repository;

import com.fluento.features.grammar.entity.GrammarExample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GrammarExampleRepository
        extends JpaRepository<GrammarExample, UUID> {

    List<GrammarExample> findByRule_IdAndDeletedAtIsNullOrderByPositionAsc(
            UUID ruleId
    );

    Optional<GrammarExample> findByIdAndDeletedAtIsNull(
            UUID id
    );
}
