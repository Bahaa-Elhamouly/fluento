CREATE TABLE user_streaks
(
    id                  UUID      NOT NULL,
    user_id             UUID      NOT NULL,
    current_streak_days INTEGER   NOT NULL DEFAULT 0,
    longest_streak_days INTEGER   NOT NULL DEFAULT 0,
    last_activity_date  DATE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at          TIMESTAMP,

    CONSTRAINT pk_user_streaks PRIMARY KEY (id),
    CONSTRAINT uk_user_streaks_user_id UNIQUE (user_id),
    CONSTRAINT fk_user_streaks_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT chk_user_streaks_current
        CHECK (current_streak_days >= 0),
    CONSTRAINT chk_user_streaks_longest
        CHECK (longest_streak_days >= 0)
);

CREATE INDEX idx_user_streaks_user_id
    ON user_streaks (user_id);
