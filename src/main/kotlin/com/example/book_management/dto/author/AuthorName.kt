package com.example.book_management.dto.author

/**
 * 著者名を表す値オブジェクト
 */
@JvmInline
value class AuthorName(val value: String) {
    init {
        require(value.isNotBlank()) { "著者名は空白にできません。" }
    }
}
