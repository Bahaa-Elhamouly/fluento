package com.fluento.features.quiz.repository;

import com.fluento.features.quiz.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, UUID> {

    long countByDeletedAtIsNull();

    long countByPassedTrueAndDeletedAtIsNull();

    @Query("""
            select coalesce(avg(attempt.percentage), 0)
            from QuizAttempt attempt
            where attempt.deletedAt is null
            """)
    double averagePercentage();
}
