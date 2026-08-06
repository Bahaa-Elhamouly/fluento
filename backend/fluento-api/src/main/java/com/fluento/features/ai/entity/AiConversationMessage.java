package com.fluento.features.ai.entity;

import com.fluento.common.entity.BaseEntity;
import com.fluento.features.ai.enums.AiMessageRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ai_conversation_messages")
public class AiConversationMessage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private AiConversationSession session;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    private AiMessageRole role;

    @Column(name = "content", nullable = false, length = 4000)
    private String content;

    @Column(name = "provider", nullable = false, length = 80)
    private String provider;

    @Column(name = "model", nullable = false, length = 120)
    private String model;
}
