-- src/main/resources/db/migration/v1__init.sql
CREATE TABLE authors(
    id UUID PRIMARY KEY
    , name VARCHAR (20) NOT NULL
    , birth_date DATE NOT NULL
    , created_at TIMESTAMP NOT NULL DEFAULT now()
    , updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE books(
    id UUID PRIMARY KEY
    , title VARCHAR (100) NOT NULL
    , price NUMERIC(12,2) NOT NULL DEFAULT 0
    , publication_status VARCHAR (20) NOT NULL DEFAULT 'UNPUBLISHED'
    , created_at TIMESTAMP NOT NULL DEFAULT now()
    , updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE book_authors (
    book_id   UUID NOT NULL REFERENCES books(id)   ON DELETE CASCADE,
    author_id UUID NOT NULL REFERENCES authors(id) ON DELETE RESTRICT,
    PRIMARY KEY (book_id, author_id)
);
