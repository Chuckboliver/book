CREATE TABLE IF NOT EXISTS book
(
    id             BIGINT NOT NULL AUTO_INCREMENT,
    title          VARCHAR(255),
    author         VARCHAR(255),
    published_date DATE,
    PRIMARY KEY (id)
);

CREATE INDEX author_idx on book (author);
