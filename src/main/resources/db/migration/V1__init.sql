CREATE TABLE authors (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    nationality VARCHAR(255) NOT NULL,
    birth_year  INT          NOT NULL
);

CREATE TABLE books (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    title          VARCHAR(200) NOT NULL,
    published_year INT          NOT NULL,
    author_id      BIGINT       NOT NULL,
    CONSTRAINT fk_books_author FOREIGN KEY (author_id) REFERENCES authors (id)
);