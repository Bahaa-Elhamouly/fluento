ALTER TABLE xp_events
    DROP CONSTRAINT chk_xp_events_type;

ALTER TABLE xp_events
    ADD CONSTRAINT chk_xp_events_type
        CHECK (event_type IN (
            'LESSON_COMPLETED',
            'QUIZ_PASSED',
            'GRAMMAR_PRACTICE_COMPLETED',
            'DAILY_GOAL_MET',
            'ACHIEVEMENT_UNLOCKED'
        ));

CREATE TABLE grammar_exercises
(
    id             UUID          NOT NULL,
    rule_id        UUID          NOT NULL,
    type           VARCHAR(40)   NOT NULL,
    prompt         VARCHAR(1000) NOT NULL,
    options_json   VARCHAR(2000),
    correct_answer VARCHAR(500)  NOT NULL,
    explanation    VARCHAR(1000),
    level          VARCHAR(40)   NOT NULL,
    points         INTEGER       NOT NULL DEFAULT 1,
    position       INTEGER       NOT NULL DEFAULT 0,
    enabled        BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at     TIMESTAMP,

    CONSTRAINT pk_grammar_exercises PRIMARY KEY (id),
    CONSTRAINT fk_grammar_exercises_rule
        FOREIGN KEY (rule_id)
            REFERENCES grammar_rules (id),
    CONSTRAINT chk_grammar_exercises_type
        CHECK (type IN (
            'MULTIPLE_CHOICE',
            'FILL_IN_THE_BLANK',
            'MATCHING'
        )),
    CONSTRAINT chk_grammar_exercises_level
        CHECK (level IN (
            'BEGINNER',
            'ELEMENTARY',
            'INTERMEDIATE',
            'UPPER_INTERMEDIATE',
            'ADVANCED'
        )),
    CONSTRAINT chk_grammar_exercises_points
        CHECK (points > 0)
);

CREATE TABLE grammar_practice_attempts
(
    id         UUID      NOT NULL,
    user_id    UUID      NOT NULL,
    rule_id    UUID      NOT NULL,
    score      INTEGER   NOT NULL,
    max_score  INTEGER   NOT NULL,
    percentage INTEGER   NOT NULL,
    xp_earned  INTEGER   NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,

    CONSTRAINT pk_grammar_practice_attempts PRIMARY KEY (id),
    CONSTRAINT fk_grammar_practice_attempts_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT fk_grammar_practice_attempts_rule
        FOREIGN KEY (rule_id)
            REFERENCES grammar_rules (id),
    CONSTRAINT chk_grammar_practice_attempts_score
        CHECK (score >= 0 AND max_score >= 0 AND percentage >= 0 AND percentage <= 100)
);

CREATE TABLE grammar_practice_attempt_answers
(
    id               UUID         NOT NULL,
    attempt_id       UUID         NOT NULL,
    exercise_id      UUID         NOT NULL,
    submitted_answer VARCHAR(500) NOT NULL,
    correct          BOOLEAN      NOT NULL DEFAULT FALSE,
    points_awarded   INTEGER      NOT NULL DEFAULT 0,
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at       TIMESTAMP,

    CONSTRAINT pk_grammar_practice_attempt_answers PRIMARY KEY (id),
    CONSTRAINT fk_grammar_practice_attempt_answers_attempt
        FOREIGN KEY (attempt_id)
            REFERENCES grammar_practice_attempts (id),
    CONSTRAINT fk_grammar_practice_attempt_answers_exercise
        FOREIGN KEY (exercise_id)
            REFERENCES grammar_exercises (id),
    CONSTRAINT chk_grammar_practice_attempt_answers_points
        CHECK (points_awarded >= 0)
);

CREATE INDEX idx_grammar_exercises_rule_id
    ON grammar_exercises (rule_id);

CREATE INDEX idx_grammar_practice_attempts_user_id
    ON grammar_practice_attempts (user_id);

CREATE INDEX idx_grammar_practice_attempts_rule_id
    ON grammar_practice_attempts (rule_id);

CREATE INDEX idx_grammar_practice_attempt_answers_attempt_id
    ON grammar_practice_attempt_answers (attempt_id);

INSERT INTO grammar_exercises (
    id,
    rule_id,
    type,
    prompt,
    options_json,
    correct_answer,
    explanation,
    level,
    points,
    position
) VALUES
    (
        'abababab-abab-abab-abab-ababababab01',
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee1',
        'MULTIPLE_CHOICE',
        'Where does the conjugated verb usually appear in a simple German statement?',
        '["First position","Second position","Last position"]',
        'Second position',
        'In simple statements, the conjugated verb normally sits in the second position.',
        'BEGINNER',
        1,
        1
    ),
    (
        'abababab-abab-abab-abab-ababababab02',
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee2',
        'FILL_IN_THE_BLANK',
        'Complete the explicit Spanish subject pronoun: ___ hablo espanol.',
        NULL,
        'Yo',
        'Yo means I. Spanish can often drop it, but beginners should recognize it.',
        'BEGINNER',
        1,
        1
    ),
    (
        'abababab-abab-abab-abab-ababababab03',
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee3',
        'FILL_IN_THE_BLANK',
        'Complete the French negation: Je ne parle ___ francais.',
        NULL,
        'pas',
        'The written beginner form places ne before the verb and pas after it.',
        'BEGINNER',
        1,
        1
    );
