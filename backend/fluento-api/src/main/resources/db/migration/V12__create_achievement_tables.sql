CREATE TABLE achievements
(
    id          UUID         NOT NULL,
    type        VARCHAR(50)  NOT NULL,
    title       VARCHAR(180) NOT NULL,
    description VARCHAR(500) NOT NULL,
    xp_reward   INTEGER      NOT NULL DEFAULT 0,
    enabled     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP,

    CONSTRAINT pk_achievements PRIMARY KEY (id),
    CONSTRAINT uk_achievements_type UNIQUE (type),
    CONSTRAINT chk_achievements_type
        CHECK (type IN (
            'FIRST_LESSON',
            'FIVE_LESSONS',
            'FIRST_QUIZ_PASS',
            'XP_100'
        )),
    CONSTRAINT chk_achievements_xp_reward
        CHECK (xp_reward >= 0)
);

CREATE TABLE user_achievements
(
    id             UUID      NOT NULL,
    user_id        UUID      NOT NULL,
    achievement_id UUID      NOT NULL,
    unlocked_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at     TIMESTAMP,

    CONSTRAINT pk_user_achievements PRIMARY KEY (id),
    CONSTRAINT uk_user_achievements_user_achievement
        UNIQUE (user_id, achievement_id),
    CONSTRAINT fk_user_achievements_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT fk_user_achievements_achievement
        FOREIGN KEY (achievement_id)
            REFERENCES achievements (id)
);

CREATE INDEX idx_user_achievements_user_id
    ON user_achievements (user_id);

CREATE INDEX idx_user_achievements_achievement_id
    ON user_achievements (achievement_id);

INSERT INTO achievements (
    id,
    type,
    title,
    description,
    xp_reward
) VALUES
    (
        '77777777-7777-7777-7777-777777777771',
        'FIRST_LESSON',
        'First Lesson',
        'Complete your first Fluento lesson.',
        25
    ),
    (
        '77777777-7777-7777-7777-777777777772',
        'FIVE_LESSONS',
        'Five Lessons',
        'Complete five Fluento lessons.',
        50
    ),
    (
        '77777777-7777-7777-7777-777777777773',
        'FIRST_QUIZ_PASS',
        'First Quiz Pass',
        'Pass your first Fluento quiz.',
        25
    ),
    (
        '77777777-7777-7777-7777-777777777774',
        'XP_100',
        '100 XP',
        'Earn 100 lesson XP.',
        50
    );
