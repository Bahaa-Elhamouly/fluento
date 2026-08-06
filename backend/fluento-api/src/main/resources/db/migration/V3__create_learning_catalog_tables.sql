CREATE TABLE languages
(
    id          UUID         NOT NULL,
    code        VARCHAR(10)  NOT NULL,
    name        VARCHAR(100) NOT NULL,
    native_name VARCHAR(100) NOT NULL,
    enabled     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP,

    CONSTRAINT pk_languages PRIMARY KEY (id),
    CONSTRAINT uk_languages_code UNIQUE (code)
);

CREATE TABLE courses
(
    id          UUID          NOT NULL,
    language_id UUID         NOT NULL,
    title       VARCHAR(160) NOT NULL,
    slug        VARCHAR(180) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    level       VARCHAR(40)  NOT NULL,
    status      VARCHAR(30)  NOT NULL DEFAULT 'DRAFT',
    position    INTEGER      NOT NULL DEFAULT 0,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP,

    CONSTRAINT pk_courses PRIMARY KEY (id),
    CONSTRAINT uk_courses_slug UNIQUE (slug),
    CONSTRAINT fk_courses_language
        FOREIGN KEY (language_id)
            REFERENCES languages (id),
    CONSTRAINT chk_courses_level
        CHECK (level IN (
            'BEGINNER',
            'ELEMENTARY',
            'INTERMEDIATE',
            'UPPER_INTERMEDIATE',
            'ADVANCED'
        )),
    CONSTRAINT chk_courses_status
        CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED'))
);

CREATE TABLE course_modules
(
    id          UUID          NOT NULL,
    course_id   UUID         NOT NULL,
    title       VARCHAR(160) NOT NULL,
    description VARCHAR(1000),
    position    INTEGER      NOT NULL DEFAULT 0,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP,

    CONSTRAINT pk_course_modules PRIMARY KEY (id),
    CONSTRAINT fk_course_modules_course
        FOREIGN KEY (course_id)
            REFERENCES courses (id)
);

CREATE INDEX idx_courses_language_id
    ON courses (language_id);

CREATE INDEX idx_courses_status
    ON courses (status);

CREATE INDEX idx_course_modules_course_id
    ON course_modules (course_id);

INSERT INTO languages (
    id,
    code,
    name,
    native_name
) VALUES
    ('11111111-1111-1111-1111-111111111111', 'de', 'German', 'Deutsch'),
    ('22222222-2222-2222-2222-222222222222', 'es', 'Spanish', 'Español'),
    ('33333333-3333-3333-3333-333333333333', 'fr', 'French', 'Français');

INSERT INTO courses (
    id,
    language_id,
    title,
    slug,
    description,
    level,
    status,
    position
) VALUES
    (
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
        '11111111-1111-1111-1111-111111111111',
        'German Foundations',
        'german-foundations',
        'Build core German vocabulary, grammar, pronunciation, and everyday conversation confidence.',
        'BEGINNER',
        'PUBLISHED',
        1
    ),
    (
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2',
        '22222222-2222-2222-2222-222222222222',
        'Spanish Foundations',
        'spanish-foundations',
        'Learn practical Spanish for introductions, travel, daily routines, and essential grammar.',
        'BEGINNER',
        'PUBLISHED',
        2
    ),
    (
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3',
        '33333333-3333-3333-3333-333333333333',
        'French Foundations',
        'french-foundations',
        'Start speaking and understanding French through structured lessons and guided practice.',
        'BEGINNER',
        'PUBLISHED',
        3
    );

INSERT INTO course_modules (
    id,
    course_id,
    title,
    description,
    position
) VALUES
    (
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1',
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
        'German Essentials',
        'Greetings, personal introductions, basic sentence order, and everyday phrases.',
        1
    ),
    (
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2',
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2',
        'Spanish Essentials',
        'Greetings, introductions, pronunciation basics, and high-frequency expressions.',
        1
    ),
    (
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb3',
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3',
        'French Essentials',
        'Greetings, introductions, pronunciation basics, and practical beginner phrases.',
        1
    );
