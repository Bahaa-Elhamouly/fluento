package com.fluento.features.grammar.repository;

import com.fluento.features.grammar.entity.GrammarPracticeAttemptAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GrammarPracticeAttemptAnswerRepository
        extends JpaRepository<GrammarPracticeAttemptAnswer, UUID> {
}
