package com.example.book_management.dto.response

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("BookUpdateResponse 単体テスト")
class BookUpdateResponseTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("正常なBookIdでBookUpdateResponseを作成できる")
        fun constructor_validBookId() {
            // Given
            val bookId = "12345678-1234-1234-1234-123456789abc"

            // When
            val response = BookUpdateResponse(bookId = bookId)

            // Then
            assertThat(response.bookId).isEqualTo(bookId)
        }
    }

    @Nested
    @DisplayName("Data Class固有テスト")
    inner class DataClassTest {

        @Test
        @DisplayName("同じ値を持つBookUpdateResponseは等価")
        fun equals_sameValues() {
            // Given
            val bookId = "12345678-1234-1234-1234-123456789abc"
            val response1 = BookUpdateResponse(bookId = bookId)
            val response2 = BookUpdateResponse(bookId = bookId)

            // Then
            assertThat(response1).isEqualTo(response2)
        }

        @Test
        @DisplayName("異なる値を持つBookUpdateResponseは不等価")
        fun equals_differentValues() {
            // Given
            val response1 = BookUpdateResponse(bookId = "12345678-1234-1234-1234-123456789abc")
            val response2 = BookUpdateResponse(bookId = "87654321-4321-4321-4321-cba987654321")

            // Then
            assertThat(response1).isNotEqualTo(response2)
        }

        @Test
        @DisplayName("同じ値を持つBookUpdateResponseは同じハッシュコード")
        fun hashCode_sameValues() {
            // Given
            val bookId = "12345678-1234-1234-1234-123456789abc"
            val response1 = BookUpdateResponse(bookId = bookId)
            val response2 = BookUpdateResponse(bookId = bookId)

            // Then
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode())
        }

        @Test
        @DisplayName("copyで別の値でコピー作成できる")
        fun copy_differentValues() {
            // Given
            val originalResponse = BookUpdateResponse(bookId = "12345678-1234-1234-1234-123456789abc")

            // When
            val copiedResponse = originalResponse.copy(bookId = "87654321-4321-4321-4321-cba987654321")

            // Then
            assertThat(copiedResponse.bookId).isEqualTo("87654321-4321-4321-4321-cba987654321")
        }
    }
}
