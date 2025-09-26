package com.example.book_management.dto.book

/**
 * 書籍タイトルを表す値オブジェクト
 */
@JvmInline
value class BookTitle(val value: String) {
    init {
        require(value.isNotBlank()) { "書籍タイトルは空白にできません。" }
    }
}
