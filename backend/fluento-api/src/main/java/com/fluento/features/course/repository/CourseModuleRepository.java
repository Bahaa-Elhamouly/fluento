package com.fluento.features.course.repository;

import com.fluento.features.course.entity.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseModuleRepository
        extends JpaRepository<CourseModule, UUID> {

    List<CourseModule> findByCourse_IdAndDeletedAtIsNullOrderByPositionAsc(
            UUID courseId
    );

    Optional<CourseModule> findByIdAndDeletedAtIsNull(UUID id);

    long countByDeletedAtIsNull();
}
