CREATE TABLE refresh_tokens
(
    id       UUID NOT NULL,
    token    VARCHAR(255),
    expiry   TIMESTAMP WITHOUT TIME ZONE,
    users_id UUID,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id)
);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT uc_refresh_tokens_users UNIQUE (users_id);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT FK_REFRESH_TOKENS_ON_USERS FOREIGN KEY (users_id) REFERENCES users (id);