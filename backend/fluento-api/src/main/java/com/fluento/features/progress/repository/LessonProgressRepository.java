package com.fluento.features.progress.repository;

import com.fluento.features.progress.entity.LessonProgress;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonProgressRepository
        extends JpaRepository<LessonProgress, UUID> {

    @EntityGraph(attributePaths = {"lesson", "lesson.module", "lesson.module.course"})
    Optional<LessonProgress> findByUser_IdAndLesson_IdAndDeletedAtIsNull(
            UUID userId,
            UUID lessonId
    );

    long countByUser_IdAndCompletedTrueAndDeletedAtIsNull(UUID userId);

    List<LessonProgress> findTop5ByUser_IdAndCompletedTrueAndDeletedAtIsNullOrderByCompletedAtDesc(
            UUID userId
    );

    @Query("""
            select coalesce(sum(progress.xpEarned), 0)
            from LessonProgress progress
            where progress.user.id = :userId
              and progress.completed = true
              and progress.deletedAt is null
            """)
    long sumXpByUserId(UUID userId);

    List<LessonProgress> findByUser_IdAndCompletedTrueAndCompletedAtGreaterThanEqualAndDeletedAtIsNull(
            UUID userId,
            LocalDateTime from
    );

    long countByCompletedTrueAndDeletedAtIsNull();
}
