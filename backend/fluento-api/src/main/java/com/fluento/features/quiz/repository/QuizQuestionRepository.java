package com.fluento.features.quiz.repository;

import com.fluento.features.quiz.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuizQuestionRepository
        extends JpaRepository<QuizQuestion, UUID> {

    List<QuizQuestion> findByQuiz_IdAndDeletedAtIsNullOrderByPositionAsc(
            UUID quizId
    );

    Optional<QuizQuestion> findByIdAndDeletedAtIsNull(
            UUID id
    );
}
