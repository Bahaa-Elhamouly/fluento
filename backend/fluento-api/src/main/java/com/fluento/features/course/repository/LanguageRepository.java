package com.fluento.features.course.repository;

import com.fluento.features.course.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LanguageRepository extends JpaRepository<Language, UUID> {

    List<Language> findByEnabledTrueAndDeletedAtIsNullOrderByNameAsc();

    Optional<Language> findByCodeAndDeletedAtIsNull(String code);

    long countByEnabledTrueAndDeletedAtIsNull();
}
