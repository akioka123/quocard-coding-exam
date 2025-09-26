package com.example.book_management.dto.author

import java.time.LocalDate

/**
 * 生年月日を表す値オブジェクト
 */
@JvmInline
value class BirthDate(val value: LocalDate) {
    init {
        require(value.isBefore(LocalDate.now())) { "生年月日は現在より過去の日付でなければなりません。" }
    }
}
