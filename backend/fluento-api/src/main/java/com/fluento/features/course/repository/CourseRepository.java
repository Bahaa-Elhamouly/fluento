package com.fluento.features.course.repository;

import com.fluento.features.course.entity.Course;
import com.fluento.features.course.enums.CourseStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    @EntityGraph(attributePaths = "language")
    List<Course> findByStatusAndDeletedAtIsNullOrderByPositionAscTitleAsc(
            CourseStatus status
    );

    @EntityGraph(attributePaths = "language")
    List<Course> findByLanguage_CodeAndStatusAndDeletedAtIsNullOrderByPositionAscTitleAsc(
            String languageCode,
            CourseStatus status
    );

    @EntityGraph(attributePaths = "language")
    Optional<Course> findBySlugAndDeletedAtIsNull(String slug);

    @EntityGraph(attributePaths = "language")
    List<Course> findByDeletedAtIsNullOrderByPositionAscTitleAsc();

    @EntityGraph(attributePaths = "language")
    Optional<Course> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsBySlugAndDeletedAtIsNull(String slug);

    long countByDeletedAtIsNull();

    long countByStatusAndDeletedAtIsNull(CourseStatus status);
}
