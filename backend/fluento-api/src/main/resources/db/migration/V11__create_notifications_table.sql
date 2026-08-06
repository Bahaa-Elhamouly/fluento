CREATE TABLE notifications
(
    id         UUID          NOT NULL,
    user_id    UUID          NOT NULL,
    type       VARCHAR(40)   NOT NULL,
    title      VARCHAR(180)  NOT NULL,
    message    VARCHAR(1000) NOT NULL,
    action_url VARCHAR(500),
    read_at    TIMESTAMP,
    created_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,

    CONSTRAINT pk_notifications PRIMARY KEY (id),
    CONSTRAINT fk_notifications_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT chk_notifications_type
        CHECK (type IN (
            'DAILY_REMINDER',
            'LESSON_REMINDER',
            'ACHIEVEMENT',
            'SYSTEM'
        ))
);

CREATE INDEX idx_notifications_user_id
    ON notifications (user_id);

CREATE INDEX idx_notifications_read_at
    ON notifications (read_at);

CREATE INDEX idx_notifications_created_at
    ON notifications (created_at);
