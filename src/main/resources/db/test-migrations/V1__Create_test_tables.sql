-- テスト用スキーマ作成
CREATE SCHEMA IF NOT EXISTS test;

-- テスト用テーブル作成（本番と同じ構造）
CREATE TABLE test.authors (
    id UUID PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    birth_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE test.books (
    id UUID PRIMARY KEY
    , title VARCHAR (100) NOT NULL
    , price NUMERIC(12,2) NOT NULL DEFAULT 0
    , publication_status VARCHAR (20) NOT NULL DEFAULT 'UNPUBLISHED'
    , created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    , updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE test.book_authors (
    book_id   UUID NOT NULL REFERENCES books(id)   ON DELETE CASCADE
    ,author_id UUID NOT NULL REFERENCES authors(id) ON DELETE RESTRICT
    ,PRIMARY KEY (book_id, author_id)
);
