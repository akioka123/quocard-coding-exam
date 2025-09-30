package com.example.book_management.dto.book

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

/**
 * 出版状況を表す値オブジェクト
 * ドメインルール: 出版済みから未出版への状態遷移は不可
 */
enum class PublicationStatus(@get:JsonValue val value: String) {
    UNPUBLISHED("UNPUBLISHED"), // 未出版
    PUBLISHED("PUBLISHED");    // 出版済み
    
    /**
     * 状態遷移の妥当性を検証する
     * ドメインルール: 出版済みから未出版への遷移は不可
     */
    fun canTransitionTo(newStatus: PublicationStatus): Boolean {
        return when (this) {
            UNPUBLISHED -> true // 未出版からは任意の状態に遷移可能
            PUBLISHED -> newStatus == PUBLISHED // 出版済みからは出版済みのみ可能
        }
    }
    
    /**
     * 状態遷移を実行する
     * 無効な遷移の場合は例外をスロー
     */
    fun transitionTo(newStatus: PublicationStatus): PublicationStatus {
        if (!canTransitionTo(newStatus)) {
            throw IllegalStateException("無効な状態遷移です: ${this.value} -> ${newStatus.value}")
        }
        return newStatus
    }
    
    companion object {
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        @JvmStatic
        fun fromValue(value: String): PublicationStatus {
            return PublicationStatus.entries.find { it.value == value }
                ?: throw IllegalArgumentException("無効な出版状況: $value")
        }
    }
}
