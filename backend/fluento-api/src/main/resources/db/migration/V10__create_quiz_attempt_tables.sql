CREATE TABLE quiz_attempts
(
    id         UUID      NOT NULL,
    user_id    UUID      NOT NULL,
    quiz_id    UUID      NOT NULL,
    score      INTEGER   NOT NULL DEFAULT 0,
    max_score  INTEGER   NOT NULL DEFAULT 0,
    percentage INTEGER   NOT NULL DEFAULT 0,
    passed     BOOLEAN   NOT NULL DEFAULT FALSE,
    xp_earned  INTEGER   NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,

    CONSTRAINT pk_quiz_attempts PRIMARY KEY (id),
    CONSTRAINT fk_quiz_attempts_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT fk_quiz_attempts_quiz
        FOREIGN KEY (quiz_id)
            REFERENCES quizzes (id),
    CONSTRAINT chk_quiz_attempts_percentage
        CHECK (percentage >= 0 AND percentage <= 100),
    CONSTRAINT chk_quiz_attempts_score
        CHECK (score >= 0 AND max_score >= 0 AND score <= max_score),
    CONSTRAINT chk_quiz_attempts_xp
        CHECK (xp_earned >= 0)
);

CREATE TABLE quiz_attempt_answers
(
    id               UUID          NOT NULL,
    attempt_id       UUID          NOT NULL,
    question_id      UUID          NOT NULL,
    submitted_answer VARCHAR(1000) NOT NULL,
    correct          BOOLEAN       NOT NULL DEFAULT FALSE,
    points_awarded   INTEGER       NOT NULL DEFAULT 0,
    created_at       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at       TIMESTAMP,

    CONSTRAINT pk_quiz_attempt_answers PRIMARY KEY (id),
    CONSTRAINT fk_quiz_attempt_answers_attempt
        FOREIGN KEY (attempt_id)
            REFERENCES quiz_attempts (id),
    CONSTRAINT fk_quiz_attempt_answers_question
        FOREIGN KEY (question_id)
            REFERENCES quiz_questions (id),
    CONSTRAINT chk_quiz_attempt_answers_points
        CHECK (points_awarded >= 0)
);

CREATE INDEX idx_quiz_attempts_user_id
    ON quiz_attempts (user_id);

CREATE INDEX idx_quiz_attempts_quiz_id
    ON quiz_attempts (quiz_id);

CREATE INDEX idx_quiz_attempt_answers_attempt_id
    ON quiz_attempt_answers (attempt_id);

CREATE INDEX idx_quiz_attempt_answers_question_id
    ON quiz_attempt_answers (question_id);
