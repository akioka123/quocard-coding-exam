package com.example.book_management.dto.book

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

/**
 * 書籍タイトルを表す値オブジェクト
 * 
 * 文字列をラップした値オブジェクトで、書籍タイトルの制約（空白不可、100文字以内）を
 * 強制する。JacksonによるJSONシリアライゼーション/デシリアライゼーションを
 * サポートし、型安全性を提供する。
 */
@JvmInline
value class BookTitle @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
constructor(@get:JsonValue val value: String) {
    init {
        require(value.isNotBlank()) { "書籍タイトルは空白にできません。" }
        require(value.length <= 100) { "書籍タイトルは100文字以内でなければなりません。" }
    }
}
