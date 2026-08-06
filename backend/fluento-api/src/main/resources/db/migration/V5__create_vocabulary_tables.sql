CREATE TABLE vocabulary_terms
(
    id            UUID          NOT NULL,
    language_id   UUID          NOT NULL,
    term          VARCHAR(160)  NOT NULL,
    translation   VARCHAR(160)  NOT NULL,
    pronunciation VARCHAR(160),
    example       VARCHAR(1000),
    tags          VARCHAR(500)  NOT NULL DEFAULT '',
    enabled       BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at    TIMESTAMP,

    CONSTRAINT pk_vocabulary_terms PRIMARY KEY (id),
    CONSTRAINT fk_vocabulary_terms_language
        FOREIGN KEY (language_id)
            REFERENCES languages (id)
);

CREATE INDEX idx_vocabulary_terms_language_id
    ON vocabulary_terms (language_id);

CREATE INDEX idx_vocabulary_terms_term
    ON vocabulary_terms (term);

INSERT INTO vocabulary_terms (
    id,
    language_id,
    term,
    translation,
    pronunciation,
    example,
    tags
) VALUES
    (
        'dddddddd-dddd-dddd-dddd-ddddddddddd1',
        '11111111-1111-1111-1111-111111111111',
        'Hallo',
        'Hello',
        'HAH-loh',
        'Hallo, ich heiße Sara.',
        'greetings,beginner'
    ),
    (
        'dddddddd-dddd-dddd-dddd-ddddddddddd2',
        '11111111-1111-1111-1111-111111111111',
        'Danke',
        'Thank you',
        'DAHN-kuh',
        'Danke für deine Hilfe.',
        'politeness,beginner'
    ),
    (
        'dddddddd-dddd-dddd-dddd-ddddddddddd3',
        '22222222-2222-2222-2222-222222222222',
        'Hola',
        'Hello',
        'OH-lah',
        'Hola, me llamo Sara.',
        'greetings,beginner'
    ),
    (
        'dddddddd-dddd-dddd-dddd-ddddddddddd4',
        '22222222-2222-2222-2222-222222222222',
        'Gracias',
        'Thank you',
        'GRAH-syahs',
        'Gracias por tu ayuda.',
        'politeness,beginner'
    ),
    (
        'dddddddd-dddd-dddd-dddd-ddddddddddd5',
        '33333333-3333-3333-3333-333333333333',
        'Bonjour',
        'Hello',
        'bohn-ZHOOR',
        'Bonjour, je m''appelle Sara.',
        'greetings,beginner'
    ),
    (
        'dddddddd-dddd-dddd-dddd-ddddddddddd6',
        '33333333-3333-3333-3333-333333333333',
        'Merci',
        'Thank you',
        'mehr-SEE',
        'Merci pour ton aide.',
        'politeness,beginner'
    );
