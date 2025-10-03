package com.example.book_management.dto.book

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@DisplayName("CreateBookWithAuthorRequest 単体テスト")
class CreateBookWithAuthorRequestTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("正常な値オブジェクトでCreateBookWithAuthorRequestを作成できる")
        fun constructor_validValueObjects() {
            // Given
            val title = BookTitle("テストタイトル")
            val price = BookPrice(BigDecimal("1500"))
            val publicationStatus = PublicationStatus.PUBLISHED

            // When
            val request = CreateBookWithAuthorRequest(
                title = title,
                bookPrice = price,
                publicationStatus = publicationStatus
            )

            // Then
            assertThat(request.title).isEqualTo(title)
            assertThat(request.bookPrice).isEqualTo(price)
            assertThat(request.publicationStatus).isEqualTo(publicationStatus)
        }

        @Test
        @DisplayName("未出版の状況でCreateBookWithAuthorRequestを作成できる")
        fun constructor_unpublishedStatus() {
            // Given
            val title = BookTitle("未出版のテストタイトル")
            val price = BookPrice(BigDecimal("1000"))
            val publicationStatus = PublicationStatus.UNPUBLISHED

            // When
            val request = CreateBookWithAuthorRequest(
                title = title,
                bookPrice = price,
                publicationStatus = publicationStatus
            )

            // Then
            assertThat(request.title).isEqualTo(title)
            assertThat(request.bookPrice).isEqualTo(price)
            assertThat(request.publicationStatus).isEqualTo(publicationStatus)
        }
    }

    @Nested
    @DisplayName("Data Class固有テスト")
    inner class DataClassTest {

        @Test
        @DisplayName("同じ値オブジェクトを持つCreateBookWithAuthorRequestは等価")
        fun equals_sameValueObjects() {
            // Given
            val title = BookTitle("テストタイトル")
            val price = BookPrice(BigDecimal("1500"))
            val publicationStatus = PublicationStatus.PUBLISHED

            val request1 = CreateBookWithAuthorRequest(
                title = title,
                bookPrice = price,
                publicationStatus = publicationStatus
            )
            val request2 = CreateBookWithAuthorRequest(
                title = title,
                bookPrice = price,
                publicationStatus = publicationStatus
            )

            // Then
            assertThat(request1).isEqualTo(request2)
        }

        @Test
        @DisplayName("異なる値オブジェクトを持つCreateBookWithAuthorRequestは不等価")
        fun equals_differentValueObjects() {
            // Given
            val request1 = CreateBookWithAuthorRequest(
                title = BookTitle("タイトル1"),
                bookPrice = BookPrice(BigDecimal("1000")),
                publicationStatus = PublicationStatus.PUBLISHED
            )
            val request2 = CreateBookWithAuthorRequest(
                title = BookTitle("タイトル2"),
                bookPrice = BookPrice(BigDecimal("2000")),
                publicationStatus = PublicationStatus.UNPUBLISHED
            )

            // Then
            assertThat(request1).isNotEqualTo(request2)
        }

        @Test
        @DisplayName("同じ値オブジェクトを持つCreateBookWithAuthorRequestは同じハッシュコード")
        fun hashCode_sameValueObjects() {
            // Given
            val title = BookTitle("テストタイトル")
            val price = BookPrice(BigDecimal("1500"))
            val publicationStatus = PublicationStatus.PUBLISHED

            val request1 = CreateBookWithAuthorRequest(
                title = title,
                bookPrice = price,
                publicationStatus = publicationStatus
            )
            val request2 = CreateBookWithAuthorRequest(
                title = title,
                bookPrice = price,
                publicationStatus = publicationStatus
            )

            // Then
            assertThat(request1.hashCode()).isEqualTo(request2.hashCode())
        }

        @Test
        @DisplayName("copyで別の値でコピー作成できる")
        fun copy_differentValues() {
            // Given
            val originalRequest = CreateBookWithAuthorRequest(
                title = BookTitle("元のタイトル"),
                bookPrice = BookPrice(BigDecimal("1500")),
                publicationStatus = PublicationStatus.PUBLISHED
            )

            // When
            val copiedRequest = originalRequest.copy(
                title = BookTitle("新しいタイトル"),
                bookPrice = BookPrice(BigDecimal("2000"))
            )

            // Then
            assertThat(copiedRequest.title).isEqualTo(BookTitle("新しいタイトル"))
            assertThat(copiedRequest.bookPrice).isEqualTo(BookPrice(BigDecimal("2000")))
            assertThat(copiedRequest.publicationStatus).isEqualTo(PublicationStatus.PUBLISHED)
        }
    }
}
