CREATE TABLE grammar_rules
(
    id          UUID          NOT NULL,
    language_id UUID         NOT NULL,
    title       VARCHAR(180) NOT NULL,
    slug        VARCHAR(200) NOT NULL,
    explanation VARCHAR(3000) NOT NULL,
    level       VARCHAR(40)  NOT NULL,
    enabled     BOOLEAN      NOT NULL DEFAULT TRUE,
    position    INTEGER      NOT NULL DEFAULT 0,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP,

    CONSTRAINT pk_grammar_rules PRIMARY KEY (id),
    CONSTRAINT uk_grammar_rules_slug UNIQUE (slug),
    CONSTRAINT fk_grammar_rules_language
        FOREIGN KEY (language_id)
            REFERENCES languages (id),
    CONSTRAINT chk_grammar_rules_level
        CHECK (level IN (
            'BEGINNER',
            'ELEMENTARY',
            'INTERMEDIATE',
            'UPPER_INTERMEDIATE',
            'ADVANCED'
        ))
);

CREATE TABLE grammar_examples
(
    id          UUID         NOT NULL,
    rule_id     UUID        NOT NULL,
    source_text VARCHAR(500) NOT NULL,
    translation VARCHAR(500) NOT NULL,
    note        VARCHAR(500),
    position    INTEGER     NOT NULL DEFAULT 0,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP,

    CONSTRAINT pk_grammar_examples PRIMARY KEY (id),
    CONSTRAINT fk_grammar_examples_rule
        FOREIGN KEY (rule_id)
            REFERENCES grammar_rules (id)
);

CREATE INDEX idx_grammar_rules_language_id
    ON grammar_rules (language_id);

CREATE INDEX idx_grammar_rules_level
    ON grammar_rules (level);

CREATE INDEX idx_grammar_examples_rule_id
    ON grammar_examples (rule_id);

INSERT INTO grammar_rules (
    id,
    language_id,
    title,
    slug,
    explanation,
    level,
    enabled,
    position
) VALUES
    (
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee1',
        '11111111-1111-1111-1111-111111111111',
        'German verb position in simple statements',
        'german-verb-position-simple-statements',
        'In a basic German statement, the conjugated verb usually appears in the second position. This helps learners build simple sentences like Ich heiße Sara.',
        'BEGINNER',
        TRUE,
        1
    ),
    (
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee2',
        '22222222-2222-2222-2222-222222222222',
        'Spanish subject pronouns',
        'spanish-subject-pronouns',
        'Spanish often drops subject pronouns because the verb ending usually shows who is acting. Beginners should still learn yo, tú, él, ella, and usted.',
        'BEGINNER',
        TRUE,
        1
    ),
    (
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee3',
        '33333333-3333-3333-3333-333333333333',
        'French basic negation',
        'french-basic-negation',
        'French commonly forms negation with ne before the verb and pas after it. In everyday speech, ne is often dropped, but learners should first recognize the full written form.',
        'BEGINNER',
        TRUE,
        1
    );

INSERT INTO grammar_examples (
    id,
    rule_id,
    source_text,
    translation,
    note,
    position
) VALUES
    (
        'ffffffff-ffff-ffff-ffff-fffffffffff1',
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee1',
        'Ich heiße Sara.',
        'My name is Sara.',
        'The verb heiße is in the second position.',
        1
    ),
    (
        'ffffffff-ffff-ffff-ffff-fffffffffff2',
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee2',
        'Yo hablo español.',
        'I speak Spanish.',
        'Yo is explicit here, but it can often be omitted.',
        1
    ),
    (
        'ffffffff-ffff-ffff-ffff-fffffffffff3',
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee3',
        'Je ne parle pas français.',
        'I do not speak French.',
        'Ne and pas surround the verb parle.',
        1
    );
