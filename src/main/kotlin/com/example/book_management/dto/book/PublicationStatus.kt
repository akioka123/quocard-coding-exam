package com.example.book_management.dto.book

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

/**
 * 出版状況を表す列挙型
 *
 * 書籍の出版状況（未出版/出版済み）を表現する列挙型。
 * ドメインルールとして「出版済みから未出版への状態遷移は不可」を実装し、
 * 状態遷移の妥当性検証と実行機能を提供する。
 */
enum class PublicationStatus(@get:JsonValue val value: String) {
    UNPUBLISHED("UNPUBLISHED"), // 未出版
    PUBLISHED("PUBLISHED");    // 出版済み

    /**
     * 状態遷移の妥当性を検証する
     *
     * 現在の状態から指定された状態への遷移が可能かどうかを判定する。
     * ドメインルールに基づき、出版済みから未出版への遷移は不可とする。
     *
     * @param newStatus 遷移先の状態
     * @return 遷移可能な場合はtrue、不可能な場合はfalse
     */
    fun canTransitionTo(newStatus: PublicationStatus): Boolean {
        return when (this) {
            UNPUBLISHED -> true // 未出版からは任意の状態に遷移可能
            PUBLISHED -> newStatus == PUBLISHED // 出版済みからは出版済みのみ可能
        }
    }

    /**
     * 状態遷移を実行する
     *
     * 現在の状態から指定された状態への遷移を実行する。
     * 無効な遷移の場合はIllegalStateExceptionをスローする。
     *
     * @param newStatus 遷移先の状態
     * @return 遷移後の状態
     * @throws IllegalStateException 無効な状態遷移の場合
     */
    fun transitionTo(newStatus: PublicationStatus): PublicationStatus {
        if (!canTransitionTo(newStatus)) {
            throw IllegalStateException("無効な状態遷移です: ${this.value} -> ${newStatus.value}")
        }
        return newStatus
    }

    companion object {
        /**
         * 文字列値からPublicationStatusを取得するファクトリーメソッド
         *
         * JSONデシリアライゼーションやAPIリクエストの文字列値から
         * 対応するPublicationStatusを取得する。無効な値の場合は
         * IllegalArgumentExceptionをスローする。
         *
         * @param value 文字列値（"UNPUBLISHED" または "PUBLISHED"）
         * @return 対応するPublicationStatus
         * @throws IllegalArgumentException 無効な値の場合
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        @JvmStatic
        fun fromValue(value: String): PublicationStatus {
            return PublicationStatus.entries.find { it.value == value }
                ?: throw IllegalArgumentException("無効な出版状況: $value")
        }
    }
}
