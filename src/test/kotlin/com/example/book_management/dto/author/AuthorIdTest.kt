package com.example.book_management.dto.author

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

@DisplayName("AuthorId 単体テスト")
class AuthorIdTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("有効なUUIDでオブジェクトが作成される")
        fun createAuthorId_validUuid() {
            // Given
            val uuid = UUID.randomUUID()

            // When
            val authorId = AuthorId(uuid)

            // Then
            assertThat(authorId.value).isEqualTo(uuid)
        }

        @Test
        @DisplayName("特定のUUIDでオブジェクトが作成される")
        fun createAuthorId_specificUuid() {
            // Given
            val uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")

            // When
            val authorId = AuthorId(uuid)

            // Then
            assertThat(authorId.value).isEqualTo(uuid)
        }

        @Test
        @DisplayName("最小値のUUIDでオブジェクトが作成される")
        fun createAuthorId_minUuid() {
            // Given
            val uuid = UUID(0L, 0L)

            // When
            val authorId = AuthorId(uuid)

            // Then
            assertThat(authorId.value).isEqualTo(uuid)
        }

        @Test
        @DisplayName("最大値のUUIDでオブジェクトが作成される")
        fun createAuthorId_maxUuid() {
            // Given
            val uuid = UUID(-1L, -1L)

            // When
            val authorId = AuthorId(uuid)

            // Then
            assertThat(authorId.value).isEqualTo(uuid)
        }
    }

    @Nested
    @DisplayName("等価性テスト")
    inner class EqualityTest {

        @Test
        @DisplayName("同じ値のAuthorIdは等価である")
        fun authorId_equality_sameValue() {
            // Given
            val uuid = UUID.randomUUID()
            val authorId1 = AuthorId(uuid)
            val authorId2 = AuthorId(uuid)

            // When & Then
            assertThat(authorId1).isEqualTo(authorId2)
            assertThat(authorId1.hashCode()).isEqualTo(authorId2.hashCode())
        }

        @Test
        @DisplayName("異なる値のAuthorIdは等価でない")
        fun authorId_equality_differentValue() {
            // Given
            val authorId1 = AuthorId(UUID.randomUUID())
            val authorId2 = AuthorId(UUID.randomUUID())

            // When & Then
            assertThat(authorId1).isNotEqualTo(authorId2)
        }
    }

    @Nested
    @DisplayName("文字列表現テスト")
    inner class StringRepresentationTest {

        @Test
        @DisplayName("toString()でUUIDの文字列表現が返される")
        fun authorId_toString() {
            // Given
            val uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
            val authorId = AuthorId(uuid)

            // When
            val result = authorId.toString()

            // Then
            assertThat(result).isEqualTo("AuthorId(value=123e4567-e89b-12d3-a456-426614174000)")
        }
    }

    @Nested
    @DisplayName("JSONシリアライゼーションテスト")
    inner class JsonSerializationTest {

        @Test
        @DisplayName("JsonValueアノテーションによりUUIDの値が返される")
        fun authorId_jsonValue() {
            // Given
            val uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
            val authorId = AuthorId(uuid)

            // When
            val result = authorId.value

            // Then
            assertThat(result).isEqualTo(uuid)
        }
    }
}

