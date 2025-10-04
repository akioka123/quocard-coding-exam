package com.example.book_management.dto.author

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.time.LocalDate

/**
 * 生年月日を表す値オブジェクト
 * 
 * LocalDateをラップした値オブジェクトで、生年月日の制約（現在日より過去）を
 * 強制する。JacksonによるJSONシリアライゼーション/デシリアライゼーションを
 * サポートし、型安全性を提供する。
 */
@JvmInline
value class BirthDate @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
constructor(@get:JsonValue val value: LocalDate) {
    init {
        require(value.isBefore(LocalDate.now())) { "生年月日は現在より過去の日付でなければなりません。" }
    }
}
