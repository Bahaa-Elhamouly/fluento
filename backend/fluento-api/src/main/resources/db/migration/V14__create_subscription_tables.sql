CREATE TABLE subscription_plans
(
    id               UUID          NOT NULL,
    code             VARCHAR(80)   NOT NULL,
    name             VARCHAR(120)  NOT NULL,
    description      VARCHAR(1000) NOT NULL,
    price            NUMERIC(10,2) NOT NULL,
    currency         VARCHAR(3)    NOT NULL,
    billing_interval VARCHAR(20)   NOT NULL,
    premium          BOOLEAN       NOT NULL DEFAULT FALSE,
    enabled          BOOLEAN       NOT NULL DEFAULT TRUE,
    position         INTEGER       NOT NULL DEFAULT 0,
    created_at       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at       TIMESTAMP,

    CONSTRAINT pk_subscription_plans PRIMARY KEY (id),
    CONSTRAINT uk_subscription_plans_code UNIQUE (code),
    CONSTRAINT chk_subscription_plans_interval
        CHECK (billing_interval IN ('MONTHLY', 'YEARLY')),
    CONSTRAINT chk_subscription_plans_price
        CHECK (price >= 0)
);

CREATE TABLE user_subscriptions
(
    id                       UUID        NOT NULL,
    user_id                  UUID        NOT NULL,
    plan_id                  UUID        NOT NULL,
    status                   VARCHAR(30) NOT NULL,
    provider_customer_id     VARCHAR(180),
    provider_subscription_id VARCHAR(180),
    current_period_start     TIMESTAMP   NOT NULL,
    current_period_end       TIMESTAMP   NOT NULL,
    cancel_at_period_end     BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at               TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at               TIMESTAMP,

    CONSTRAINT pk_user_subscriptions PRIMARY KEY (id),
    CONSTRAINT fk_user_subscriptions_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT fk_user_subscriptions_plan
        FOREIGN KEY (plan_id)
            REFERENCES subscription_plans (id),
    CONSTRAINT chk_user_subscriptions_status
        CHECK (status IN (
            'TRIALING',
            'ACTIVE',
            'PAST_DUE',
            'CANCELED',
            'EXPIRED'
        )),
    CONSTRAINT chk_user_subscriptions_period
        CHECK (current_period_end > current_period_start)
);

CREATE TABLE payment_events
(
    id                 UUID          NOT NULL,
    user_id            UUID          NOT NULL,
    subscription_id    UUID,
    provider           VARCHAR(80)   NOT NULL,
    provider_event_id  VARCHAR(180)  NOT NULL,
    status             VARCHAR(30)   NOT NULL,
    amount             NUMERIC(10,2) NOT NULL,
    currency           VARCHAR(3)    NOT NULL,
    metadata_json      VARCHAR(2000),
    created_at         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at         TIMESTAMP,

    CONSTRAINT pk_payment_events PRIMARY KEY (id),
    CONSTRAINT fk_payment_events_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT fk_payment_events_subscription
        FOREIGN KEY (subscription_id)
            REFERENCES user_subscriptions (id),
    CONSTRAINT uk_payment_events_provider_event
        UNIQUE (provider, provider_event_id),
    CONSTRAINT chk_payment_events_status
        CHECK (status IN ('PENDING', 'SUCCEEDED', 'FAILED', 'REFUNDED')),
    CONSTRAINT chk_payment_events_amount
        CHECK (amount >= 0)
);

CREATE INDEX idx_user_subscriptions_user_id
    ON user_subscriptions (user_id);

CREATE INDEX idx_user_subscriptions_status
    ON user_subscriptions (status);

CREATE INDEX idx_payment_events_user_id
    ON payment_events (user_id);

CREATE INDEX idx_payment_events_subscription_id
    ON payment_events (subscription_id);

INSERT INTO subscription_plans (
    id,
    code,
    name,
    description,
    price,
    currency,
    billing_interval,
    premium,
    position
) VALUES
    (
        '55555555-5555-5555-5555-555555555551',
        'free',
        'Free',
        'Core lessons, public catalog access, and beginner practice.',
        0.00,
        'USD',
        'MONTHLY',
        FALSE,
        1
    ),
    (
        '55555555-5555-5555-5555-555555555552',
        'pro-monthly',
        'Pro Monthly',
        'Premium practice, advanced analytics, AI tutor access, and expanded exercises.',
        9.99,
        'USD',
        'MONTHLY',
        TRUE,
        2
    ),
    (
        '55555555-5555-5555-5555-555555555553',
        'pro-yearly',
        'Pro Yearly',
        'Annual premium access with AI tutor, analytics, and expanded practice.',
        99.99,
        'USD',
        'YEARLY',
        TRUE,
        3
    );
