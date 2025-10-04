package com.example.book_management.dto.author

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

/**
 * 著者名を表す値オブジェクト
 * 
 * 文字列をラップした値オブジェクトで、著者名の制約（空白不可、20文字以内）を
 * 強制する。JacksonによるJSONシリアライゼーション/デシリアライゼーションを
 * サポートし、型安全性を提供する。
 */
@JvmInline
value class AuthorName @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
constructor(@get:JsonValue val value: String) {
    init {
        val maxSize = 20
        require(value.isNotBlank()) { "著者名は空白にできません。" }
        require(value.length <= maxSize) { "著者名は20文字以内にしてください" }
    }
}
