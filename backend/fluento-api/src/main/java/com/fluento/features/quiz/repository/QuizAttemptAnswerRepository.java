package com.fluento.features.quiz.repository;

import com.fluento.features.quiz.entity.QuizAttemptAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuizAttemptAnswerRepository
        extends JpaRepository<QuizAttemptAnswer, UUID> {
}
