package com.fluento.features.quiz.repository;

import com.fluento.features.quiz.entity.Quiz;
import com.fluento.features.quiz.enums.QuizStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuizRepository extends JpaRepository<Quiz, UUID> {

    @EntityGraph(attributePaths = "lesson")
    Optional<Quiz> findFirstByLesson_IdAndStatusAndDeletedAtIsNullOrderByPositionAsc(
            UUID lessonId,
            QuizStatus status
    );

    @EntityGraph(attributePaths = "lesson")
    Optional<Quiz> findByIdAndStatusAndDeletedAtIsNull(
            UUID id,
            QuizStatus status
    );

    @EntityGraph(attributePaths = "lesson")
    List<Quiz> findByLesson_IdAndDeletedAtIsNullOrderByPositionAsc(
            UUID lessonId
    );

    @EntityGraph(attributePaths = "lesson")
    Optional<Quiz> findByIdAndDeletedAtIsNull(
            UUID id
    );

    long countByDeletedAtIsNull();

    long countByStatusAndDeletedAtIsNull(QuizStatus status);
}
