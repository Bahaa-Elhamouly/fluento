CREATE TABLE user_vocabulary
(
    id               UUID      NOT NULL,
    user_id          UUID      NOT NULL,
    term_id          UUID      NOT NULL,
    favorite         BOOLEAN   NOT NULL DEFAULT FALSE,
    mastery_level    INTEGER   NOT NULL DEFAULT 0,
    last_reviewed_at TIMESTAMP,
    created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at       TIMESTAMP,

    CONSTRAINT pk_user_vocabulary PRIMARY KEY (id),
    CONSTRAINT uk_user_vocabulary_user_term UNIQUE (user_id, term_id),
    CONSTRAINT fk_user_vocabulary_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT fk_user_vocabulary_term
        FOREIGN KEY (term_id)
            REFERENCES vocabulary_terms (id),
    CONSTRAINT chk_user_vocabulary_mastery
        CHECK (mastery_level >= 0 AND mastery_level <= 5)
);

CREATE INDEX idx_user_vocabulary_user_id
    ON user_vocabulary (user_id);

CREATE INDEX idx_user_vocabulary_term_id
    ON user_vocabulary (term_id);

CREATE INDEX idx_user_vocabulary_favorite
    ON user_vocabulary (favorite);
