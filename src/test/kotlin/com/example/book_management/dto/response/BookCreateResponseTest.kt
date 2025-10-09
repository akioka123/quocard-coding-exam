package com.example.book_management.dto.response

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("BookCreateResponse 単体テスト")
class BookCreateResponseTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("正常なBookIdでBookCreateResponseを作成できる")
        fun constructor_validBookId() {
            // Given
            val bookId = "12345678-1234-1234-1234-123456789abc"

            // When
            val response = BookCreateResponse(bookId = bookId)

            // Then
            assertThat(response.bookId).isEqualTo(bookId)
        }
    }

    @Nested
    @DisplayName("Data Class固有テスト")
    inner class DataClassTest {

        @Test
        @DisplayName("同じ値を持つBookCreateResponseは等価")
        fun equals_sameValues() {
            // Given
            val bookId = "12345678-1234-1234-1234-123456789abc"
            val response1 = BookCreateResponse(bookId = bookId)
            val response2 = BookCreateResponse(bookId = bookId)

            // Then
            assertThat(response1).isEqualTo(response2)
        }

        @Test
        @DisplayName("異なる値を持つBookCreateResponseは不等価")
        fun equals_differentValues() {
            // Given
            val response1 = BookCreateResponse(bookId = "12345678-1234-1234-1234-123456789abc")
            val response2 = BookCreateResponse(bookId = "87654321-4321-4321-4321-cba987654321")

            // Then
            assertThat(response1).isNotEqualTo(response2)
        }

        @Test
        @DisplayName("同じ値を持つBookCreateResponseは同じハッシュコード")
        fun hashCode_sameValues() {
            // Given
            val bookId = "12345678-1234-1234-1234-123456789abc"
            val response1 = BookCreateResponse(bookId = bookId)
            val response2 = BookCreateResponse(bookId = bookId)

            // Then
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode())
        }

        @Test
        @DisplayName("copyで別の値でコピー作成できる")
        fun copy_differentValues() {
            // Given
            val originalResponse = BookCreateResponse(bookId = "12345678-1234-1234-1234-123456789abc")

            // When
            val copiedResponse = originalResponse.copy(bookId = "87654321-4321-4321-4321-cba987654321")

            // Then
            assertThat(copiedResponse.bookId).isEqualTo("87654321-4321-4321-4321-cba987654321")
        }
    }
}
