CREATE TABLE ai_conversation_sessions
(
    id          UUID         NOT NULL,
    user_id     UUID         NOT NULL,
    language_id UUID         NOT NULL,
    title       VARCHAR(180) NOT NULL,
    active      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP,

    CONSTRAINT pk_ai_conversation_sessions PRIMARY KEY (id),
    CONSTRAINT fk_ai_conversation_sessions_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT fk_ai_conversation_sessions_language
        FOREIGN KEY (language_id)
            REFERENCES languages (id)
);

CREATE TABLE ai_conversation_messages
(
    id         UUID          NOT NULL,
    session_id UUID          NOT NULL,
    role       VARCHAR(30)   NOT NULL,
    content    VARCHAR(4000) NOT NULL,
    provider   VARCHAR(80)   NOT NULL,
    model      VARCHAR(120)  NOT NULL,
    created_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,

    CONSTRAINT pk_ai_conversation_messages PRIMARY KEY (id),
    CONSTRAINT fk_ai_conversation_messages_session
        FOREIGN KEY (session_id)
            REFERENCES ai_conversation_sessions (id),
    CONSTRAINT chk_ai_conversation_messages_role
        CHECK (role IN ('USER', 'TUTOR', 'SYSTEM'))
);

CREATE INDEX idx_ai_conversation_sessions_user_id
    ON ai_conversation_sessions (user_id);

CREATE INDEX idx_ai_conversation_sessions_language_id
    ON ai_conversation_sessions (language_id);

CREATE INDEX idx_ai_conversation_messages_session_id
    ON ai_conversation_messages (session_id);
