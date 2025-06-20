CREATE TABLE api_keys
(
    id       UUID NOT NULL,
    key      VARCHAR(255),
    users_id UUID,
    CONSTRAINT pk_api_keys PRIMARY KEY (id)
);

ALTER TABLE api_keys
    ADD CONSTRAINT uc_api_keys_users UNIQUE (users_id);

ALTER TABLE api_keys
    ADD CONSTRAINT FK_API_KEYS_ON_USERS FOREIGN KEY (users_id) REFERENCES users (id);