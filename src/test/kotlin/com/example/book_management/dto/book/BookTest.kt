package com.example.book_management.dto.book

import com.example.book_management.dto.author.AuthorId
import com.example.book_management.tables.records.BooksRecord
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@DisplayName("Book 単体テスト")
class BookTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("正常な値でBookを作成できる")
        fun constructor_validValues() {
            // Given
            val id = BookId(UUID.randomUUID())
            val title = BookTitle("テストタイトル")
            val bookPrice = BookPrice(BigDecimal("1500"))
            val authorIds = listOf(AuthorId(UUID.randomUUID()))
            val publicationStatus = PublicationStatus.PUBLISHED
            val createdAt = LocalDateTime.now().minusDays(1)
            val updatedAt = LocalDateTime.now()

            // When
            val book = Book(
                id = id,
                title = title,
                bookPrice = bookPrice,
                authorIds = authorIds,
                publicationStatus = publicationStatus,
                createdAt = createdAt,
                updatedAt = updatedAt
            )

            // Then
            assertThat(book.id).isEqualTo(id)
            assertThat(book.title).isEqualTo(title)
            assertThat(book.bookPrice).isEqualTo(bookPrice)
            assertThat(book.authorIds).isEqualTo(authorIds)
            assertThat(book.publicationStatus).isEqualTo(publicationStatus)
            assertThat(book.createdAt).isEqualTo(createdAt)
            assertThat(book.updatedAt).isEqualTo(updatedAt)
        }

        @Test
        @DisplayName("複数の著者でBookを作成できる")
        fun constructor_multipleAuthors() {
            // Given
            val id = BookId(UUID.randomUUID())
            val title = BookTitle("テストタイトル")
            val bookPrice = BookPrice(BigDecimal("1500"))
            val authorIds = listOf(
                AuthorId(UUID.randomUUID()),
                AuthorId(UUID.randomUUID()),
                AuthorId(UUID.randomUUID())
            )
            val publicationStatus = PublicationStatus.UNPUBLISHED
            val createdAt = LocalDateTime.now().minusDays(1)
            val updatedAt = LocalDateTime.now()

            // When
            val book = Book(
                id = id,
                title = title,
                bookPrice = bookPrice,
                authorIds = authorIds,
                publicationStatus = publicationStatus,
                createdAt = createdAt,
                updatedAt = updatedAt
            )

            // Then
            assertThat(book.authorIds).hasSize(3)
            assertThat(book.authorIds).isEqualTo(authorIds)
        }

        @Test
        @DisplayName("BooksRecordからBookを作成できる")
        fun fromRecord_validRecord() {
            // Given
            val id = UUID.randomUUID()
            val record = BooksRecord()
            record.id = id
            record.title = "テストタイトル"
            record.price = BigDecimal("1500")
            record.publicationStatus = "PUBLISHED"
            record.createdAt = LocalDateTime.now().minusDays(1)
            record.updatedAt = LocalDateTime.now()
            val authors = listOf(AuthorId(UUID.randomUUID()))

            // When
            val book = Book.fromRecord(record, authors)

            // Then
            assertThat(book.id.value).isEqualTo(id)
            assertThat(book.title.value).isEqualTo("テストタイトル")
            assertThat(book.bookPrice.value).isEqualTo(BigDecimal("1500"))
            assertThat(book.publicationStatus).isEqualTo(PublicationStatus.PUBLISHED)
            assertThat(book.authorIds).isEqualTo(authors)
        }
    }

    @Nested
    @DisplayName("異常系テスト")
    inner class AbnormalTest {

        @Test
        @DisplayName("空の著者リストでBook作成時に例外発生")
        fun constructor_emptyAuthorsList() {
            // Given
            val id = BookId(UUID.randomUUID())
            val title = BookTitle("テストタイトル")
            val bookPrice = BookPrice(BigDecimal("1500"))
            val authors = emptyList<AuthorId>()
            val publicationStatus = PublicationStatus.PUBLISHED
            val createdAt = LocalDateTime.now().minusDays(1)
            val updatedAt = LocalDateTime.now()

            // When & Then
            assertThatThrownBy {
                Book(
                    id = id,
                    title = title,
                    bookPrice = bookPrice,
                    authorIds = authors,
                    publicationStatus = publicationStatus,
                    createdAt = createdAt,
                    updatedAt = updatedAt
                )
            }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("書籍には最低1人の著者が必要です。")
        }

        @Test
        @DisplayName("nullのBookIdでレコードから作成時に例外発生")
        fun fromRecord_nullBookId() {
            // Given
            val record = BooksRecord()
            record.id = null // nullのID
            record.title = "テストタイトル"
            record.price = BigDecimal("1500")
            record.publicationStatus = "PUBLISHED"
            record.createdAt = LocalDateTime.now().minusDays(1)
            record.updatedAt = LocalDateTime.now()

            // When & Then
            assertThatThrownBy { Book.fromRecord(record) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("書籍IDがnullです")
        }

        @Test
        @DisplayName("nullのタイトルでレコードから作成時に例外発生")
        fun fromRecord_nullTitle() {
            // Given
            val record = BooksRecord()
            record.id = UUID.randomUUID()
            record.title = null // nullのタイトル
            record.price = BigDecimal("1500")
            record.publicationStatus = "PUBLISHED"
            record.createdAt = LocalDateTime.now().minusDays(1)
            record.updatedAt = LocalDateTime.now()

            // When & Then
            assertThatThrownBy { Book.fromRecord(record) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("書籍タイトルがnullです")
        }

        @Test
        @DisplayName("nullの価格でレコードから作成時に例外発生")
        fun fromRecord_nullPrice() {
            // Given
            val record = BooksRecord()
            record.id = UUID.randomUUID()
            record.title = "テストタイトル"
            record.price = null // nullの価格
            record.publicationStatus = "PUBLISHED"
            record.createdAt = LocalDateTime.now().minusDays(1)
            record.updatedAt = LocalDateTime.now()

            // When & Then
            assertThatThrownBy { Book.fromRecord(record) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("価格がnullです")
        }

        @Test
        @DisplayName("nullの出版状況でレコードから作成時に例外発生")
        fun fromRecord_nullPublicationStatus() {
            // Given
            val record = BooksRecord()
            record.id = UUID.randomUUID()
            record.title = "テストタイトル"
            record.price = BigDecimal("1500")
            record.publicationStatus = null // nullの出版状況
            record.createdAt = LocalDateTime.now().minusDays(1)
            record.updatedAt = LocalDateTime.now()

            // When & Then
            assertThatThrownBy { Book.fromRecord(record) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("出版状況がnullです")
        }

        @Test
        @DisplayName("nullの作成日時でレコードから作成時に例外発生")
        fun fromRecord_nullCreatedAt() {
            // Given
            val record = BooksRecord()
            record.id = UUID.randomUUID()
            record.title = "テストタイトル"
            record.price = BigDecimal("1500")
            record.publicationStatus = "PUBLISHED"
            record.createdAt = null // nullの作成日時
            record.updatedAt = LocalDateTime.now()

            // When & Then
            assertThatThrownBy { Book.fromRecord(record) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("作成日時がnullです")
        }

        @Test
        @DisplayName("nullの更新日時でレコードから作成時に例外発生")
        fun fromRecord_nullUpdatedAt() {
            // Given
            val record = BooksRecord()
            record.id = UUID.randomUUID()
            record.title = "テストタイトル"
            record.price = BigDecimal("1500")
            record.publicationStatus = "PUBLISHED"
            record.createdAt = LocalDateTime.now().minusDays(1)
            record.updatedAt = null // nullの更新日時

            // When & Then
            assertThatThrownBy { Book.fromRecord(record) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("更新日時がnullです")
        }

    }

    @Nested
    @DisplayName("Data Class固有テスト")
    inner class DataClassTest {

        @Test
        @DisplayName("同じ値を持つBookは等価")
        fun equals_sameValues() {
            // Given
            val id = BookId(UUID.randomUUID())
            val title = BookTitle("テストタイトル")
            val bookPrice = BookPrice(BigDecimal("1500"))
            val authors = listOf(AuthorId(UUID.randomUUID()))
            val publicationStatus = PublicationStatus.PUBLISHED
            val createdAt = LocalDateTime.now().minusDays(1)
            val updatedAt = LocalDateTime.now()

            val book1 = Book(
                id = id,
                title = title,
                bookPrice = bookPrice,
                authorIds = authors,
                publicationStatus = publicationStatus,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
            val book2 = Book(
                id = id,
                title = title,
                bookPrice = bookPrice,
                authorIds = authors,
                publicationStatus = publicationStatus,
                createdAt = createdAt,
                updatedAt = updatedAt
            )

            // Then
            assertThat(book1).isEqualTo(book2)
        }

        @Test
        @DisplayName("異なる値を持つBookは不等価")
        fun equals_differentValues() {
            // Given
            val book1 = Book(
                id = BookId(UUID.randomUUID()),
                title = BookTitle("タイトル1"),
                bookPrice = BookPrice(BigDecimal("1000")),
                authorIds = listOf(AuthorId(UUID.randomUUID())),
                publicationStatus = PublicationStatus.PUBLISHED,
                createdAt = LocalDateTime.now().minusDays(1),
                updatedAt = LocalDateTime.now()
            )
            val book2 = Book(
                id = BookId(UUID.randomUUID()),
                title = BookTitle("タイトル2"),
                bookPrice = BookPrice(BigDecimal("2000")),
                authorIds = listOf(AuthorId(UUID.randomUUID())),
                publicationStatus = PublicationStatus.UNPUBLISHED,
                createdAt = LocalDateTime.now().minusDays(2),
                updatedAt = LocalDateTime.now().minusHours(1)
            )

            // Then
            assertThat(book1).isNotEqualTo(book2)
        }

        @Test
        @DisplayName("同じ値を持つBookは同じハッシュコード")
        fun hashCode_sameValues() {
            // Given
            val id = BookId(UUID.randomUUID())
            val title = BookTitle("テストタイトル")
            val bookPrice = BookPrice(BigDecimal("1500"))
            val authorIds = listOf(AuthorId(UUID.randomUUID()))
            val publicationStatus = PublicationStatus.PUBLISHED
            val createdAt = LocalDateTime.now().minusDays(1)
            val updatedAt = LocalDateTime.now()

            val book1 = Book(
                id = id,
                title = title,
                bookPrice = bookPrice,
                authorIds = authorIds,
                publicationStatus = publicationStatus,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
            val book2 = Book(
                id = id,
                title = title,
                bookPrice = bookPrice,
                authorIds = authorIds,
                publicationStatus = publicationStatus,
                createdAt = createdAt,
                updatedAt = updatedAt
            )

            // Then
            assertThat(book1.hashCode()).isEqualTo(book2.hashCode())
        }

        @Test
        @DisplayName("copyで別の値でコピー作成できる")
        fun copy_differentValues() {
            // Given
            val originalBook = Book(
                id = BookId(UUID.randomUUID()),
                title = BookTitle("元のタイトル"),
                bookPrice = BookPrice(BigDecimal("1500")),
                authorIds = listOf(AuthorId(UUID.randomUUID())),
                publicationStatus = PublicationStatus.PUBLISHED,
                createdAt = LocalDateTime.now().minusDays(1),
                updatedAt = LocalDateTime.now()
            )

            // When
            val copiedBook = originalBook.copy(
                title = BookTitle("新しいタイトル"),
                bookPrice = BookPrice(BigDecimal("2000"))
            )

            // Then
            assertThat(copiedBook.id).isEqualTo(originalBook.id)
            assertThat(copiedBook.title.value).isEqualTo("新しいタイトル")
            assertThat(copiedBook.bookPrice.value).isEqualTo(BigDecimal("2000"))
            assertThat(copiedBook.authorIds).isEqualTo(originalBook.authorIds)
            assertThat(copiedBook.publicationStatus).isEqualTo(originalBook.publicationStatus)
            assertThat(copiedBook.createdAt).isEqualTo(originalBook.createdAt)
            assertThat(copiedBook.updatedAt).isEqualTo(originalBook.updatedAt)
        }
    }
}
