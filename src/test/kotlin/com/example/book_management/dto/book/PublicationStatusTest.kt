package com.example.book_management.dto.book

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("PublicationStatus 単体テスト")
class PublicationStatusTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("UNPUBLISHEDから任意の状態に遷移できる")
        fun canTransitionTo_unpublishedToPublished() {
            // Given
            val currentStatus = PublicationStatus.UNPUBLISHED
            val newStatus = PublicationStatus.PUBLISHED

            // When
            val result = currentStatus.canTransitionTo(newStatus)

            // Then
            assertThat(result).isTrue()
        }

        @Test
        @DisplayName("UNPUBLISHEDから同じ状態に遷移できる")
        fun canTransitionTo_unpublishedToUnpublished() {
            // Given
            val currentStatus = PublicationStatus.UNPUBLISHED
            val newStatus = PublicationStatus.UNPUBLISHED

            // When
            val result = currentStatus.canTransitionTo(newStatus)

            // Then
            assertThat(result).isTrue()
        }

        @Test
        @DisplayName("PUBLISHEDから同じ状態に遷移できる")
        fun canTransitionTo_publishedToPublished() {
            // Given
            val currentStatus = PublicationStatus.PUBLISHED
            val newStatus = PublicationStatus.PUBLISHED

            // When
            val result = currentStatus.canTransitionTo(newStatus)
            assertThat(result).isTrue()
        }

        @Test
        @DisplayName("UNPUBLISHEDからPUBLISHEDへの状態遷移を実行できる")
        fun transitionTo_unpublishedToPublished() {
            // Given
            val currentStatus = PublicationStatus.UNPUBLISHED
            val newStatus = PublicationStatus.PUBLISHED

            // When
            val result = currentStatus.transitionTo(newStatus)

            // Then
            assertThat(result).isEqualTo(PublicationStatus.PUBLISHED)
        }

        @Test
        @DisplayName("同じ状態への遷移を実行できる")
        fun transitionTo_sameStatus() {
            // Given
            val currentStatus = PublicationStatus.UNPUBLISHED

            // When
            val result = currentStatus.transitionTo(currentStatus)

            // Then
            assertThat(result).isEqualTo(PublicationStatus.UNPUBLISHED)
        }

        @Test
        @DisplayName("UNPUBLISHEDの値オブジェクト作成")
        fun fromValue_unpublished() {
            // Given
            val value = "UNPUBLISHED"

            // When
            val result = PublicationStatus.fromValue(value)

            // Then
            assertThat(result).isEqualTo(PublicationStatus.UNPUBLISHED)
        }

        @Test
        @DisplayName("PUBLISHEDの値オブジェクト作成")
        fun fromValue_published() {
            // Given
            val value = "PUBLISHED"

            // When
            val result = PublicationStatus.fromValue(value)

            // Then
            assertThat(result).isEqualTo(PublicationStatus.PUBLISHED)
        }

        @Test
        @DisplayName("JSON値の取得確認 - UNPUBLISHED")
        fun jsonValue_unpublished() {
            // Given
            val status = PublicationStatus.UNPUBLISHED

            // When
            val result = status.value

            // Then
            assertThat(result).isEqualTo("UNPUBLISHED")
        }

        @Test
        @DisplayName("JSON値の取得確認 - PUBLISHED")
        fun jsonValue_published() {
            // Given
            val status = PublicationStatus.PUBLISHED

            // When
            val result = status.value

            // Then
            assertThat(result).isEqualTo("PUBLISHED")
        }
    }

    @Nested
    @DisplayName("異常系テスト")
    inner class AbnormalTest {

        @Test
        @DisplayName("PUBLISHEDからUNPUBLISHEDへの遷移は不可能")
        fun canTransitionTo_publishedToUnpublished() {
            // Given
            val currentStatus = PublicationStatus.PUBLISHED
            val newStatus = PublicationStatus.UNPUBLISHED

            // When
            val result = currentStatus.canTransitionTo(newStatus)

            // Then
            assertThat(result).isFalse()
        }

        @Test
        @DisplayName("PUBLISHEDからUNPUBLISHEDへの状態遷移で例外発生")
        fun transitionTo_publishedToUnpublished() {
            // Given
            val currentStatus = PublicationStatus.PUBLISHED
            val newStatus = PublicationStatus.UNPUBLISHED

            // When & Then
            assertThatThrownBy { currentStatus.transitionTo(newStatus) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("無効な状態遷移です: PUBLISHED -> UNPUBLISHED")
        }

        @Test
        @DisplayName("無効な値でのfromValue呼び出し")
        fun fromValue_invalidValue() {
            // Given
            val value = "INVALID_STATUS"

            // When & Then
            assertThatThrownBy { PublicationStatus.fromValue(value) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("無効な出版状況: INVALID_STATUS")
        }

        @Test
        @DisplayName("空文字でのfromValue呼び出し")
        fun fromValue_emptyString() {
            // Given
            val value = ""

            // When & Then
            assertThatThrownBy { PublicationStatus.fromValue(value) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("無効な出版状況: ")
        }
    }
    
    @Nested
    @DisplayName("境界値テスト")
    inner class BoundaryTest {

        @Test
        @DisplayName("大文字小文字混在での値確認 - lowercase")
        fun fromValue_lowercase() {
            // Given
            val value = "unpublished"

            // When & Then
            assertThatThrownBy { PublicationStatus.fromValue(value) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("無効な出版状況: unpublished")
        }

        @Test
        @DisplayName("大文字小文字混在での値確認 - mixed case")
        fun fromValue_mixedCase() {
            // Given
            val value = "Unpublished"

            // When & Then
            assertThatThrownBy { PublicationStatus.fromValue(value) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("無効な出版状況: Unpublished")
        }
    }

    @Nested
    @DisplayName("等価性・比較テスト")
    inner class EqualityTest {

        @Test
        @DisplayName("同じ値のPublicationStatusは等価")
        fun equality_sameValue() {
            // Given
            val status1 = PublicationStatus.UNPUBLISHED
            val status2 = PublicationStatus.UNPUBLISHED

            // When & Then
            assertThat(status1).isEqualTo(status2)
            assertThat(status1.hashCode()).isEqualTo(status2.hashCode())
        }

        @Test
        @DisplayName("異なる値のPublicationStatusは不等価")
        fun equality_differentValue() {
            // Given
            val status1 = PublicationStatus.UNPUBLISHED
            val status2 = PublicationStatus.PUBLISHED

            // When & Then
            assertThat(status1).isNotEqualTo(status2)
            assertThat(status1.hashCode()).isNotEqualTo(status2.hashCode())
        }

        @Test
        @DisplayName("enum定数の比較")
        fun enumComparison() {
            // Given
            val unpublished = PublicationStatus.UNPUBLISHED
            val published = PublicationStatus.PUBLISHED

            // When & Then
            assertThat(unpublished.name).isEqualTo("UNPUBLISHED")
            assertThat(published.name).isEqualTo("PUBLISHED")
        }
    }
}

