package com.fluento.features.lesson.repository;

import com.fluento.features.lesson.entity.Lesson;
import com.fluento.features.lesson.enums.LessonStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    @EntityGraph(attributePaths = {"module", "module.course"})
    List<Lesson> findByModule_IdAndStatusAndDeletedAtIsNullOrderByPositionAsc(
            UUID moduleId,
            LessonStatus status
    );

    @EntityGraph(attributePaths = {"module", "module.course"})
    Optional<Lesson> findByIdAndStatusAndDeletedAtIsNull(
            UUID id,
            LessonStatus status
    );

    @EntityGraph(attributePaths = {"module", "module.course"})
    Optional<Lesson> findByIdAndDeletedAtIsNull(UUID id);

    @EntityGraph(attributePaths = {"module", "module.course"})
    List<Lesson> findByModule_IdAndDeletedAtIsNullOrderByPositionAsc(
            UUID moduleId
    );

    long countByDeletedAtIsNull();

    long countByStatusAndDeletedAtIsNull(LessonStatus status);
}
