package com.fluento.features.ai.repository;

import com.fluento.features.ai.entity.AiConversationMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AiConversationMessageRepository
        extends JpaRepository<AiConversationMessage, UUID> {

    List<AiConversationMessage> findBySession_IdAndDeletedAtIsNullOrderByCreatedAtAsc(
            UUID sessionId
    );
}
