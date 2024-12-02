CREATE SCHEMA IF NOT EXISTS person;

CREATE TABLE person.users (
    id BIGSERIAL            PRIMARY KEY,
    login VARCHAR(255)      NOT NULL    UNIQUE,
    role VARCHAR(31)        NOT NULL    DEFAULT 'USER',
    language VARCHAR(2)     NOT NULL    DEFAULT 'EN',
    name VARCHAR(127)       NOT NULL    DEFAULT 'default_username',
    status VARCHAR(63)      NOT NULL    DEFAULT 'ACTIVE' CHECK (status IN('ACTIVE', 'BLOCKED', 'DISABLED')),
    created_at DATE         NOT NULL    DEFAULT now(),
    updated_at TIMESTAMP    NOT NULL    DEFAULT now()
);

CREATE TABLE person.risk_profile (
    id UUID    PRIMARY KEY  DEFAULT gen_random_uuid(),
    deal_loss_percentage SMALLINT,
    account_loss_percentage SMALLINT,
    futures_in_account_percentage SMALLINT,
    stocks_in_account_percentage SMALLINT,
    risk_type VARCHAR(127)  DEFAULT 'medium'  CHECK (risk_type IN('conservative', 'medium', 'aggressive')),
    created_at DATE         NOT NULL    DEFAULT now(),
    updated_at TIMESTAMP    NOT NULL    DEFAULT now()
);

CREATE TABLE person.account (
    id UUID    PRIMARY KEY  DEFAULT gen_random_uuid(),
    user_id BIGINT          NOT NULL    REFERENCES person.users ON DELETE CASCADE,
    client_id VARCHAR(127)  NOT NULL,
    broker VARCHAR(127)     NOT NULL,
    token VARCHAR(255)      NOT NULL,
    token_expires_at DATE,
    risk_profile_id UUID                REFERENCES person.risk_profile,
    created_at DATE         NOT NULL    DEFAULT now(),
    updated_at TIMESTAMP    NOT NULL    DEFAULT now(),
    UNIQUE(client_id, broker)
);

CREATE TABLE person.telegram (
    chat_id BIGINT  PRIMARY KEY,
    user_id BIGINT  NOT NULL    REFERENCES person.users ON DELETE CASCADE,
    created_at DATE NOT NULL    DEFAULT now()
);
