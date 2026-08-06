CREATE TABLE lessons
(
    id                UUID          NOT NULL,
    module_id         UUID          NOT NULL,
    title             VARCHAR(180)  NOT NULL,
    description       VARCHAR(1000),
    type              VARCHAR(40)   NOT NULL,
    status            VARCHAR(30)   NOT NULL DEFAULT 'DRAFT',
    content           VARCHAR(5000) NOT NULL,
    estimated_minutes INTEGER       NOT NULL DEFAULT 5,
    position          INTEGER       NOT NULL DEFAULT 0,
    created_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at        TIMESTAMP,

    CONSTRAINT pk_lessons PRIMARY KEY (id),
    CONSTRAINT fk_lessons_module
        FOREIGN KEY (module_id)
            REFERENCES course_modules (id),
    CONSTRAINT chk_lessons_type
        CHECK (type IN (
            'READING',
            'LISTENING',
            'VOCABULARY',
            'GRAMMAR',
            'EXERCISE'
        )),
    CONSTRAINT chk_lessons_status
        CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED'))
);

CREATE INDEX idx_lessons_module_id
    ON lessons (module_id);

CREATE INDEX idx_lessons_status
    ON lessons (status);

INSERT INTO lessons (
    id,
    module_id,
    title,
    description,
    type,
    status,
    content,
    estimated_minutes,
    position
) VALUES
    (
        'cccccccc-cccc-cccc-cccc-ccccccccccc1',
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1',
        'German greetings and introductions',
        'Learn essential German greetings and how to introduce yourself clearly.',
        'READING',
        'PUBLISHED',
        'Hallo means hello. Guten Morgen means good morning. To introduce yourself, say: Ich heiße Sara. Practice greeting someone, sharing your name, and asking Wie heißt du?',
        8,
        1
    ),
    (
        'cccccccc-cccc-cccc-cccc-ccccccccccc2',
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2',
        'Spanish greetings and introductions',
        'Learn essential Spanish greetings and how to introduce yourself clearly.',
        'READING',
        'PUBLISHED',
        'Hola means hello. Buenos días means good morning. To introduce yourself, say: Me llamo Sara. Practice greeting someone, sharing your name, and asking ¿Cómo te llamas?',
        8,
        1
    ),
    (
        'cccccccc-cccc-cccc-cccc-ccccccccccc3',
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb3',
        'French greetings and introductions',
        'Learn essential French greetings and how to introduce yourself clearly.',
        'READING',
        'PUBLISHED',
        'Bonjour means hello or good morning. Salut is an informal hello. To introduce yourself, say: Je m''appelle Sara. Practice greeting someone, sharing your name, and asking Comment tu t''appelles ?',
        8,
        1
    );
