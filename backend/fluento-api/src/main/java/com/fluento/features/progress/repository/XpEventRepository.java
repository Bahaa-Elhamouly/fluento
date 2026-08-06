package com.fluento.features.progress.repository;

import com.fluento.features.progress.entity.XpEvent;
import com.fluento.features.progress.enums.XpEventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface XpEventRepository extends JpaRepository<XpEvent, UUID> {

    long countByUser_IdAndEventTypeAndDeletedAtIsNull(
            UUID userId,
            XpEventType eventType
    );

    @Query("""
            select coalesce(sum(event.xp), 0)
            from XpEvent event
            where event.deletedAt is null
            """)
    long sumTotalXp();
}
