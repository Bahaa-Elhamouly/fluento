package com.fluento.features.grammar.repository;

import com.fluento.features.grammar.entity.GrammarExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GrammarExerciseRepository
        extends JpaRepository<GrammarExercise, UUID> {

    List<GrammarExercise> findByRule_IdAndEnabledTrueAndDeletedAtIsNullOrderByPositionAsc(
            UUID ruleId
    );
}
