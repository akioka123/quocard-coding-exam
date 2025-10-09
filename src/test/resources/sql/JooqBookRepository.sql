-- テスト用の著者データを事前登録
INSERT INTO test.authors (id, name, birth_date, created_at, updated_at) VALUES
('11111111-1111-1111-1111-111111111111', 'テスト著者1', '1990-01-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('22222222-2222-2222-2222-222222222222', 'テスト著者2', '1991-02-02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('33333333-3333-3333-3333-333333333333', 'テスト著者3', '1992-03-03', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- テスト用の書籍データを事前登録
INSERT INTO test.books (id, title, price, publication_status, created_at, updated_at) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'テスト書籍1', 1000.00, 'PUBLISHED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'テスト書籍2', 2000.00, 'UNPUBLISHED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'テスト書籍3', 3000.00, 'PUBLISHED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- テスト用の書籍著者関連データを事前登録
INSERT INTO test.book_authors (book_id, author_id) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111'),
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '22222222-2222-2222-2222-222222222222'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '11111111-1111-1111-1111-111111111111'),
('cccccccc-cccc-cccc-cccc-cccccccccccc', '33333333-3333-3333-3333-333333333333');
