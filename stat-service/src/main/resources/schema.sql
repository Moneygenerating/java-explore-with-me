CREATE TABLE IF NOT EXISTS hit (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app VARCHAR(255) NOT NULL,
    uri VARCHAR(512) NOT NULL,
    ip VARCHAR(100) NOT NULL,
    timestamp TIMESTAMP NULL,
    CONSTRAINT pk_hit PRIMARY KEY (id)
);