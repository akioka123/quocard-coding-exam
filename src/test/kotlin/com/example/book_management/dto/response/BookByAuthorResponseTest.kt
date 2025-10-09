package com.example.book_management.dto.response

import com.example.book_management.dto.author.AuthorId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

@DisplayName("BookByAuthorResponse 単体テスト")
class BookByAuthorResponseTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("正常な値でBookByAuthorResponseを作成できる")
        fun constructor_validValues() {
            // Given
            val bookId = "12345678-1234-1234-1234-123456789abc"
            val title = "テスト書籍"
            val price = BigDecimal("1500")
            val publicationStatus = "PUBLISHED"
            val authorIds = listOf(AuthorId(UUID.randomUUID()))

            // When
            val response = BookByAuthorResponse(
                bookId = bookId,
                title = title,
                price = price,
                publicationStatus = publicationStatus,
                authorIds = authorIds
            )

            // Then
            assertThat(response.bookId).isEqualTo(bookId)
            assertThat(response.title).isEqualTo(title)
            assertThat(response.price).isEqualTo(price)
            assertThat(response.publicationStatus).isEqualTo(publicationStatus)
            assertThat(response.authorIds).isEqualTo(authorIds)
        }

        @Test
        @DisplayName("複数の著者IDでBookByAuthorResponseを作成できる")
        fun constructor_multipleAuthorIds() {
            // Given
            val bookId = "12345678-1234-1234-1234-123456789abc"
            val title = "共著書籍"
            val price = BigDecimal("2000")
            val publicationStatus = "UNPUBLISHED"
            val authorIds = listOf(
                AuthorId(UUID.randomUUID()),
                AuthorId(UUID.randomUUID()),
                AuthorId(UUID.randomUUID())
            )

            // When
            val response = BookByAuthorResponse(
                bookId = bookId,
                title = title,
                price = price,
                publicationStatus = publicationStatus,
                authorIds = authorIds
            )

            // Then
            assertThat(response.authorIds).hasSize(3)
            assertThat(response.authorIds).containsExactlyElementsOf(authorIds)
        }

        @Test
        @DisplayName("空の著者IDリストでBookByAuthorResponseを作成できる")
        fun constructor_emptyAuthorIds() {
            // Given
            val bookId = "12345678-1234-1234-1234-123456789abc"
            val title = "テスト書籍"
            val price = BigDecimal("1000")
            val publicationStatus = "PUBLISHED"
            val authorIds = emptyList<AuthorId>()

            // When
            val response = BookByAuthorResponse(
                bookId = bookId,
                title = title,
                price = price,
                publicationStatus = publicationStatus,
                authorIds = authorIds
            )

            // Then
            assertThat(response.authorIds).isEmpty()
        }

        @Test
        @DisplayName("長いタイトルでBookByAuthorResponseを作成できる")
        fun constructor_longTitle() {
            // Given
            val bookId = "12345678-1234-1234-1234-123456789abc"
            val title = "A".repeat(100) // 長いタイトル
            val price = BigDecimal("3000")
            val publicationStatus = "PUBLISHED"
            val authorIds = listOf(AuthorId(UUID.randomUUID()))

            // When
            val response = BookByAuthorResponse(
                bookId = bookId,
                title = title,
                price = price,
                publicationStatus = publicationStatus,
                authorIds = authorIds
            )

            // Then
            assertThat(response.title).isEqualTo(title)
            assertThat(response.title).hasSize(100)
        }

        @Test
        @DisplayName("高額な価格でBookByAuthorResponseを作成できる")
        fun constructor_highPrice() {
            // Given
            val bookId = "12345678-1234-1234-1234-123456789abc"
            val title = "高級書籍"
            val price = BigDecimal("999999.99")
            val publicationStatus = "PUBLISHED"
            val authorIds = listOf(AuthorId(UUID.randomUUID()))

            // When
            val response = BookByAuthorResponse(
                bookId = bookId,
                title = title,
                price = price,
                publicationStatus = publicationStatus,
                authorIds = authorIds
            )

            // Then
            assertThat(response.price).isEqualTo(price)
        }

        @Test
        @DisplayName("UNPUBLISHEDステータスでBookByAuthorResponseを作成できる")
        fun constructor_unpublishedStatus() {
            // Given
            val bookId = "12345678-1234-1234-1234-123456789abc"
            val title = "未出版書籍"
            val price = BigDecimal("500")
            val publicationStatus = "UNPUBLISHED"
            val authorIds = listOf(AuthorId(UUID.randomUUID()))

            // When
            val response = BookByAuthorResponse(
                bookId = bookId,
                title = title,
                price = price,
                publicationStatus = publicationStatus,
                authorIds = authorIds
            )

            // Then
            assertThat(response.publicationStatus).isEqualTo("UNPUBLISHED")
        }
    }

    @Nested
    @DisplayName("Data Class固有テスト")
    inner class DataClassTest {

        @Test
        @DisplayName("同じ値を持つBookByAuthorResponseは等価")
        fun equals_sameValues() {
            // Given
            val bookId = "12345678-1234-1234-1234-123456789abc"
            val title = "テスト書籍"
            val price = BigDecimal("1500")
            val publicationStatus = "PUBLISHED"
            val authorIds = listOf(AuthorId(UUID.randomUUID()))

            val response1 = BookByAuthorResponse(
                bookId = bookId,
                title = title,
                price = price,
                publicationStatus = publicationStatus,
                authorIds = authorIds
            )
            val response2 = BookByAuthorResponse(
                bookId = bookId,
                title = title,
                price = price,
                publicationStatus = publicationStatus,
                authorIds = authorIds
            )

            // Then
            assertThat(response1).isEqualTo(response2)
        }

        @Test
        @DisplayName("異なる値を持つBookByAuthorResponseは不等価")
        fun equals_differentValues() {
            // Given
            val response1 = BookByAuthorResponse(
                bookId = "12345678-1234-1234-1234-123456789abc",
                title = "書籍1",
                price = BigDecimal("1000"),
                publicationStatus = "PUBLISHED",
                authorIds = listOf(AuthorId(UUID.randomUUID()))
            )
            val response2 = BookByAuthorResponse(
                bookId = "87654321-4321-4321-4321-cba987654321",
                title = "書籍2",
                price = BigDecimal("2000"),
                publicationStatus = "UNPUBLISHED",
                authorIds = listOf(AuthorId(UUID.randomUUID()))
            )

            // Then
            assertThat(response1).isNotEqualTo(response2)
        }

        @Test
        @DisplayName("同じ値を持つBookByAuthorResponseは同じハッシュコード")
        fun hashCode_sameValues() {
            // Given
            val bookId = "12345678-1234-1234-1234-123456789abc"
            val title = "テスト書籍"
            val price = BigDecimal("1500")
            val publicationStatus = "PUBLISHED"
            val authorIds = listOf(AuthorId(UUID.randomUUID()))

            val response1 = BookByAuthorResponse(
                bookId = bookId,
                title = title,
                price = price,
                publicationStatus = publicationStatus,
                authorIds = authorIds
            )
            val response2 = BookByAuthorResponse(
                bookId = bookId,
                title = title,
                price = price,
                publicationStatus = publicationStatus,
                authorIds = authorIds
            )

            // Then
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode())
        }

        @Test
        @DisplayName("copyで別の値でコピー作成できる")
        fun copy_differentValues() {
            // Given
            val originalResponse = BookByAuthorResponse(
                bookId = "12345678-1234-1234-1234-123456789abc",
                title = "元のタイトル",
                price = BigDecimal("1000"),
                publicationStatus = "PUBLISHED",
                authorIds = listOf(AuthorId(UUID.randomUUID()))
            )

            // When
            val copiedResponse = originalResponse.copy(
                title = "新しいタイトル",
                price = BigDecimal("2000"),
                publicationStatus = "UNPUBLISHED"
            )

            // Then
            assertThat(copiedResponse.bookId).isEqualTo(originalResponse.bookId)
            assertThat(copiedResponse.title).isEqualTo("新しいタイトル")
            assertThat(copiedResponse.price).isEqualTo(BigDecimal("2000"))
            assertThat(copiedResponse.publicationStatus).isEqualTo("UNPUBLISHED")
            assertThat(copiedResponse.authorIds).isEqualTo(originalResponse.authorIds)
        }

        @Test
        @DisplayName("copyで著者IDリストを変更できる")
        fun copy_differentAuthorIds() {
            // Given
            val originalAuthorIds = listOf(AuthorId(UUID.randomUUID()))
            val originalResponse = BookByAuthorResponse(
                bookId = "12345678-1234-1234-1234-123456789abc",
                title = "テスト書籍",
                price = BigDecimal("1000"),
                publicationStatus = "PUBLISHED",
                authorIds = originalAuthorIds
            )
            val newAuthorIds = listOf(
                AuthorId(UUID.randomUUID()),
                AuthorId(UUID.randomUUID())
            )

            // When
            val copiedResponse = originalResponse.copy(authorIds = newAuthorIds)

            // Then
            assertThat(copiedResponse.authorIds).isEqualTo(newAuthorIds)
            assertThat(copiedResponse.authorIds).hasSize(2)
        }
    }
}
