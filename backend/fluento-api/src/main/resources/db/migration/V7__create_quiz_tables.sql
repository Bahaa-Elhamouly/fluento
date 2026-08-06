CREATE TABLE quizzes
(
    id            UUID          NOT NULL,
    lesson_id     UUID          NOT NULL,
    title         VARCHAR(180)  NOT NULL,
    description   VARCHAR(1000),
    status        VARCHAR(30)   NOT NULL DEFAULT 'DRAFT',
    passing_score INTEGER       NOT NULL DEFAULT 70,
    position      INTEGER       NOT NULL DEFAULT 0,
    created_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at    TIMESTAMP,

    CONSTRAINT pk_quizzes PRIMARY KEY (id),
    CONSTRAINT fk_quizzes_lesson
        FOREIGN KEY (lesson_id)
            REFERENCES lessons (id),
    CONSTRAINT chk_quizzes_status
        CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED'))
);

CREATE TABLE quiz_questions
(
    id             UUID          NOT NULL,
    quiz_id        UUID          NOT NULL,
    type           VARCHAR(40)   NOT NULL,
    prompt         VARCHAR(1000) NOT NULL,
    options_json   VARCHAR(2000),
    correct_answer VARCHAR(500)  NOT NULL,
    explanation    VARCHAR(1000),
    points         INTEGER       NOT NULL DEFAULT 1,
    position       INTEGER       NOT NULL DEFAULT 0,
    created_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at     TIMESTAMP,

    CONSTRAINT pk_quiz_questions PRIMARY KEY (id),
    CONSTRAINT fk_quiz_questions_quiz
        FOREIGN KEY (quiz_id)
            REFERENCES quizzes (id),
    CONSTRAINT chk_quiz_questions_type
        CHECK (type IN (
            'MULTIPLE_CHOICE',
            'FILL_IN_THE_BLANK',
            'MATCHING'
        ))
);

CREATE INDEX idx_quizzes_lesson_id
    ON quizzes (lesson_id);

CREATE INDEX idx_quizzes_status
    ON quizzes (status);

CREATE INDEX idx_quiz_questions_quiz_id
    ON quiz_questions (quiz_id);

INSERT INTO quizzes (
    id,
    lesson_id,
    title,
    description,
    status,
    passing_score,
    position
) VALUES
    (
        '99999999-9999-9999-9999-999999999991',
        'cccccccc-cccc-cccc-cccc-ccccccccccc1',
        'German greetings check',
        'Check understanding of beginner German greetings.',
        'PUBLISHED',
        70,
        1
    ),
    (
        '99999999-9999-9999-9999-999999999992',
        'cccccccc-cccc-cccc-cccc-ccccccccccc2',
        'Spanish greetings check',
        'Check understanding of beginner Spanish greetings.',
        'PUBLISHED',
        70,
        1
    ),
    (
        '99999999-9999-9999-9999-999999999993',
        'cccccccc-cccc-cccc-cccc-ccccccccccc3',
        'French greetings check',
        'Check understanding of beginner French greetings.',
        'PUBLISHED',
        70,
        1
    );

INSERT INTO quiz_questions (
    id,
    quiz_id,
    type,
    prompt,
    options_json,
    correct_answer,
    explanation,
    points,
    position
) VALUES
    (
        '88888888-8888-8888-8888-888888888881',
        '99999999-9999-9999-9999-999999999991',
        'MULTIPLE_CHOICE',
        'What does Hallo mean?',
        '["Hello","Goodbye","Thanks","Please"]',
        'Hello',
        'Hallo is the standard German greeting for hello.',
        1,
        1
    ),
    (
        '88888888-8888-8888-8888-888888888882',
        '99999999-9999-9999-9999-999999999992',
        'MULTIPLE_CHOICE',
        'What does Hola mean?',
        '["Hello","Good night","Sorry","Water"]',
        'Hello',
        'Hola is the standard Spanish greeting for hello.',
        1,
        1
    ),
    (
        '88888888-8888-8888-8888-888888888883',
        '99999999-9999-9999-9999-999999999993',
        'MULTIPLE_CHOICE',
        'What does Bonjour mean?',
        '["Hello","Goodbye","See you soon","Excuse me"]',
        'Hello',
        'Bonjour is a common French greeting used for hello or good morning.',
        1,
        1
    );
