package com.fluento.features.grammar.repository;

import com.fluento.features.grammar.entity.GrammarPracticeAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GrammarPracticeAttemptRepository
        extends JpaRepository<GrammarPracticeAttempt, UUID> {
}
