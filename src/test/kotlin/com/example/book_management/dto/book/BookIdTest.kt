package com.example.book_management.dto.book

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

@DisplayName("BookId 単体テスト")
class BookIdTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("正常なUUIDでBookIdを作成できる")
        fun constructor_validUuid() {
            // Given
            val uuid = UUID.randomUUID()

            // When
            val bookId = BookId(uuid)

            // Then
            assertThat(bookId.value).isEqualTo(uuid)
        }
    }

    @Nested
    @DisplayName("等価性・比較テスト")
    inner class EqualityTest {

        @Test
        @DisplayName("同じUUIDを持つBookIdは等価")
        fun equals_sameValue() {
            // Given
            val uuid = UUID.randomUUID()
            val bookId1 = BookId(uuid)
            val bookId2 = BookId(uuid)

            // then
            assertThat(bookId1).isEqualTo(bookId2)
        }

        @Test
        @DisplayName("異なるUUIDを持つBookIdは不等価")
        fun equals_differentValue() {
            // Given
            val uuid1 = UUID.randomUUID()
            val uuid2 = UUID.randomUUID()
            val bookId1 = BookId(uuid1)
            val bookId2 = BookId(uuid2)

            // Then
            assertThat(bookId1).isNotEqualTo(bookId2)
        }

        @Test
        @DisplayName("同じUUIDを持つBookIdは同じハッシュコード")
        fun hashCode_SameValue() {
            // Given
            val uuid = UUID.randomUUID()
            val bookId1 = BookId(uuid)
            val bookId2 = BookId(uuid)

            // Then
            assertThat(bookId1.hashCode()).isEqualTo(bookId2.hashCode())
        }
    }
}
