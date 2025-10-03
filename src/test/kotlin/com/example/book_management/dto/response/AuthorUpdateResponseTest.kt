package com.example.book_management.dto.response

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("AuthorUpdateResponse 単体テスト")
class AuthorUpdateResponseTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("正常なAuthorIdでAuthorUpdateResponseを作成できる")
        fun constructor_validAuthorId() {
            // Given
            val authorId = "12345678-1234-1234-1234-123456789abc"

            // When
            val response = AuthorUpdateResponse(authorId = authorId)

            // Then
            assertThat(response.authorId).isEqualTo(authorId)
        }
    }

    @Nested
    @DisplayName("Data Class固有テスト")
    inner class DataClassTest {

        @Test
        @DisplayName("同じ値を持つAuthorUpdateResponseは等価")
        fun equals_sameValues() {
            // Given
            val authorId = "12345678-1234-1234-1234-123456789abc"
            val response1 = AuthorUpdateResponse(authorId = authorId)
            val response2 = AuthorUpdateResponse(authorId = authorId)

            // Then
            assertThat(response1).isEqualTo(response2)
        }

        @Test
        @DisplayName("異なる値を持つAuthorUpdateResponseは不等価")
        fun equals_differentValues() {
            // Given
            val response1 = AuthorUpdateResponse(authorId = "12345678-1234-1234-1234-123456789abc")
            val response2 = AuthorUpdateResponse(authorId = "87654321-4321-4321-4321-cba987654321")

            // Then
            assertThat(response1).isNotEqualTo(response2)
        }

        @Test
        @DisplayName("同じ値を持つAuthorUpdateResponseは同じハッシュコード")
        fun hashCode_sameValues() {
            // Given
            val authorId = "12345678-1234-1234-1234-123456789abc"
            val response1 = AuthorUpdateResponse(authorId = authorId)
            val response2 = AuthorUpdateResponse(authorId = authorId)

            // Then
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode())
        }

        @Test
        @DisplayName("copyで別の値でコピー作成できる")
        fun copy_differentValues() {
            // Given
            val originalResponse = AuthorUpdateResponse(authorId = "12345678-1234-1234-1234-123456789abc")

            // When
            val copiedResponse = originalResponse.copy(authorId = "87654321-4321-4321-4321-cba987654321")

            // Then
            assertThat(copiedResponse.authorId).isEqualTo("87654321-4321-4321-4321-cba987654321")
        }
    }
}
