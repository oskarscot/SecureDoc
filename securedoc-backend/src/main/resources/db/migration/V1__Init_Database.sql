CREATE TABLE collections
(
    id   UUID NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_collections PRIMARY KEY (id)
);

CREATE TABLE files
(
    id             UUID NOT NULL,
    original_name  VARCHAR(255),
    hashed_name    VARCHAR(255),
    collections_id UUID,
    users_id       UUID,
    created_at     TIMESTAMP WITHOUT TIME ZONE,
    updated_at     TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_files PRIMARY KEY (id)
);

CREATE TABLE refresh_tokens
(
    id       UUID NOT NULL,
    token    VARCHAR(255),
    expiry   TIMESTAMP WITHOUT TIME ZONE,
    users_id UUID,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id)
);

CREATE TABLE users
(
    id         UUID NOT NULL,
    username   VARCHAR(255),
    email      VARCHAR(255),
    password   VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE files
    ADD CONSTRAINT FK_FILES_ON_COLLECTIONS FOREIGN KEY (collections_id) REFERENCES collections (id);

ALTER TABLE files
    ADD CONSTRAINT FK_FILES_ON_USERS FOREIGN KEY (users_id) REFERENCES users (id);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT FK_REFRESH_TOKENS_ON_USERS FOREIGN KEY (users_id) REFERENCES users (id);