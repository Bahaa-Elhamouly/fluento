package com.fluento.features.progress.repository;

import com.fluento.features.progress.entity.Achievement;
import com.fluento.features.progress.enums.AchievementType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AchievementRepository extends JpaRepository<Achievement, UUID> {

    List<Achievement> findByEnabledTrueAndDeletedAtIsNullOrderByTitleAsc();

    Optional<Achievement> findByTypeAndEnabledTrueAndDeletedAtIsNull(
            AchievementType type
    );
}
