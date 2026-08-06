package com.fluento.features.ai.repository;

import com.fluento.features.ai.entity.AiConversationSession;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AiConversationSessionRepository
        extends JpaRepository<AiConversationSession, UUID> {

    @EntityGraph(attributePaths = "language")
    List<AiConversationSession> findTop20ByUser_IdAndActiveTrueAndDeletedAtIsNullOrderByUpdatedAtDesc(
            UUID userId
    );

    @EntityGraph(attributePaths = "language")
    Optional<AiConversationSession> findByIdAndUser_IdAndActiveTrueAndDeletedAtIsNull(
            UUID sessionId,
            UUID userId
    );
}
