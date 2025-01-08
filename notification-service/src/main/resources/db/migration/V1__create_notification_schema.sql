CREATE SCHEMA IF NOT EXISTS notification;

CREATE TABLE notification.subscriber (
    account_id UUID         PRIMARY KEY,
    email VARCHAR(255)      NOT NULL,
    email_notify boolean,
    telegram_id BIGINT,
    telegram_notify boolean,
    language VARCHAR(2)     NOT NULL    DEFAULT 'EN',
    name VARCHAR(127)                   DEFAULT 'default_username',
    created_at DATE         NOT NULL    DEFAULT now(),
    updated_at TIMESTAMP    NOT NULL    DEFAULT now(),
    UNIQUE(account_id, email, telegram_id)
);
