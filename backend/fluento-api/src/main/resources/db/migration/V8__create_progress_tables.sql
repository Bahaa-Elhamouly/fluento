CREATE TABLE lesson_progress
(
    id           UUID      NOT NULL,
    user_id      UUID      NOT NULL,
    lesson_id    UUID      NOT NULL,
    completed    BOOLEAN   NOT NULL DEFAULT FALSE,
    completed_at TIMESTAMP,
    score        INTEGER,
    xp_earned    INTEGER   NOT NULL DEFAULT 0,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at   TIMESTAMP,

    CONSTRAINT pk_lesson_progress PRIMARY KEY (id),
    CONSTRAINT uk_lesson_progress_user_lesson UNIQUE (user_id, lesson_id),
    CONSTRAINT fk_lesson_progress_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT fk_lesson_progress_lesson
        FOREIGN KEY (lesson_id)
            REFERENCES lessons (id),
    CONSTRAINT chk_lesson_progress_score
        CHECK (score IS NULL OR (score >= 0 AND score <= 100))
);

CREATE TABLE xp_events
(
    id          UUID         NOT NULL,
    user_id     UUID         NOT NULL,
    event_type  VARCHAR(40)  NOT NULL,
    source_id   UUID,
    xp          INTEGER      NOT NULL,
    description VARCHAR(300) NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP,

    CONSTRAINT pk_xp_events PRIMARY KEY (id),
    CONSTRAINT fk_xp_events_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT chk_xp_events_type
        CHECK (event_type IN (
            'LESSON_COMPLETED',
            'QUIZ_PASSED',
            'DAILY_GOAL_MET',
            'ACHIEVEMENT_UNLOCKED'
        )),
    CONSTRAINT chk_xp_events_xp_positive
        CHECK (xp >= 0)
);

CREATE INDEX idx_lesson_progress_user_id
    ON lesson_progress (user_id);

CREATE INDEX idx_lesson_progress_lesson_id
    ON lesson_progress (lesson_id);

CREATE INDEX idx_lesson_progress_completed_at
    ON lesson_progress (completed_at);

CREATE INDEX idx_xp_events_user_id
    ON xp_events (user_id);

CREATE INDEX idx_xp_events_source_id
    ON xp_events (source_id);
