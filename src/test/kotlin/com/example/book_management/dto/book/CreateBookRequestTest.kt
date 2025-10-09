package com.example.book_management.dto.book

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

@DisplayName("CreateBookRequest 単体テスト")
class CreateBookRequestTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("正常な値でCreateBookRequestを作成できる")
        fun constructor_validValues() {
            // Given
            val title = "テストタイトル"
            val price = BigDecimal("1500")
            val publicationStatus = "PUBLISHED"
            val authorIds = listOf(UUID.randomUUID(), UUID.randomUUID())

            // When
            val request = CreateBookRequest(
                title = title,
                price = price,
                publicationStatus = publicationStatus,
                authorIds = authorIds
            )

            // Then
            assertThat(request.title).isEqualTo(title)
            assertThat(request.price).isEqualTo(price)
            assertThat(request.publicationStatus).isEqualTo(publicationStatus)
            assertThat(request.authorIds).isEqualTo(authorIds)
        }

        @Test
        @DisplayName("空の著者リストでCreateBookRequestを作成できる")
        fun constructor_emptyAuthorList() {
            // Given
            val title = "テストタイトル"
            val price = BigDecimal("1500")
            val publicationStatus = "UNPUBLISHED"
            val authorIds = emptyList<UUID>()

            // When
            val request = CreateBookRequest(
                title = title,
                price = price,
                publicationStatus = publicationStatus,
                authorIds = authorIds
            )

            // Then
            assertThat(request.title).isEqualTo(title)
            assertThat(request.price).isEqualTo(price)
            assertThat(request.publicationStatus).isEqualTo(publicationStatus)
            assertThat(request.authorIds).isEmpty()
        }
    }

    @Nested
    @DisplayName("Data Class固有テスト")
    inner class DataClassTest {

        @Test
        @DisplayName("同じ値を持つCreateBookRequestは等価")
        fun equals_sameValues() {
            // Given
            val uuid = UUID.randomUUID()
            val request1 = CreateBookRequest(
                title = "テストタイトル",
                price = BigDecimal("1500"),
                publicationStatus = "PUBLISHED",
                authorIds = listOf(uuid)
            )
            val request2 = CreateBookRequest(
                title = "テストタイトル",
                price = BigDecimal("1500"),
                publicationStatus = "PUBLISHED",
                authorIds = listOf(uuid)
            )

            // Then
            assertThat(request1).isEqualTo(request2)
        }

        @Test
        @DisplayName("異なる値を持つCreateBookRequestは不等価")
        fun equals_differentValues() {
            // Given
            val request1 = CreateBookRequest(
                title = "テストタイトル1",
                price = BigDecimal("1500"),
                publicationStatus = "PUBLISHED",
                authorIds = listOf(UUID.randomUUID())
            )
            val request2 = CreateBookRequest(
                title = "テストタイトル2",
                price = BigDecimal("2000"),
                publicationStatus = "UNPUBLISHED",
                authorIds = listOf(UUID.randomUUID())
            )

            // Then
            assertThat(request1).isNotEqualTo(request2)
        }

        @Test
        @DisplayName("同じ値を持つCreateBookRequestは同じハッシュコード")
        fun hashCode_sameValues() {
            // Given
            val uuid = UUID.randomUUID()
            val request1 = CreateBookRequest(
                title = "テストタイトル",
                price = BigDecimal("1500"),
                publicationStatus = "PUBLISHED",
                authorIds = listOf(uuid)
            )
            val request2 = CreateBookRequest(
                title = "テストタイトル",
                price = BigDecimal("1500"),
                publicationStatus = "PUBLISHED",
                authorIds = listOf(uuid)
            )

            // Then
            assertThat(request1.hashCode()).isEqualTo(request2.hashCode())
        }

        @Test
        @DisplayName("copyで別の値でコピー作成できる")
        fun copy_differentValues() {
            // Given
            val originalRequest = CreateBookRequest(
                title = "元のタイトル",
                price = BigDecimal("1500"),
                publicationStatus = "PUBLISHED",
                authorIds = listOf(UUID.randomUUID())
            )

            // When
            val copiedRequest = originalRequest.copy(
                title = "新しいタイトル",
                price = BigDecimal("2000")
            )

            // Then
            assertThat(copiedRequest.title).isEqualTo("新しいタイトル")
            assertThat(copiedRequest.price).isEqualTo(BigDecimal("2000"))
            assertThat(copiedRequest.publicationStatus).isEqualTo("PUBLISHED")
            assertThat(copiedRequest.authorIds).isEqualTo(originalRequest.authorIds)
        }
    }
}
