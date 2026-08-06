CREATE TABLE user_profiles
(
    id                    UUID      NOT NULL,
    user_id               UUID      NOT NULL,
    avatar_url            VARCHAR(500),
    native_language_code  VARCHAR(10),
    target_language_codes VARCHAR(200) NOT NULL DEFAULT '',
    learning_level        VARCHAR(40),
    daily_goal_minutes    INTEGER,
    created_at            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at            TIMESTAMP,

    CONSTRAINT pk_user_profiles PRIMARY KEY (id),
    CONSTRAINT uk_user_profiles_user_id UNIQUE (user_id),
    CONSTRAINT fk_user_profiles_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT chk_user_profiles_learning_level
        CHECK (
            learning_level IS NULL
            OR learning_level IN (
                'BEGINNER',
                'ELEMENTARY',
                'INTERMEDIATE',
                'UPPER_INTERMEDIATE',
                'ADVANCED'
            )
        ),
    CONSTRAINT chk_user_profiles_daily_goal
        CHECK (
            daily_goal_minutes IS NULL
            OR (daily_goal_minutes >= 5 AND daily_goal_minutes <= 240)
        )
);

CREATE INDEX idx_user_profiles_user_id
    ON user_profiles (user_id);

CREATE INDEX idx_user_profiles_native_language_code
    ON user_profiles (native_language_code);
