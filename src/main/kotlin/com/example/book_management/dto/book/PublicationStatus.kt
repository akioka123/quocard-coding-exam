package com.example.book_management.dto.book

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

/**
 * 出版状況を表す値オブジェクト
 */
enum class PublicationStatus(@get:JsonValue val value: String) {
    UNPUBLISHED("UNPUBLISHED"), // 未出版
    PUBLISHED("PUBLISHED");    // 出版済み
    
    companion object {
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        @JvmStatic
        fun fromValue(value: String): PublicationStatus {
            return PublicationStatus.entries.find { it.value == value }
                ?: throw IllegalArgumentException("無効な出版状況: $value")
        }
    }
}
