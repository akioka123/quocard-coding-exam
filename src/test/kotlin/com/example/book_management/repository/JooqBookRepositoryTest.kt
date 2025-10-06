package com.example.book_management.repository

import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.book.*
import com.example.book_management.tables.references.BOOKS
import example.testconfig.JooqTestSchemaConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@ActiveProfiles("test")
@Import(JooqTestSchemaConfig::class)
@DisplayName("JooqBookRepository 統合テスト")
@Sql(scripts = ["/sql/JooqBookRepository.sql"])
class JooqBookRepositoryTest : BaseRepositoryTest() {

    @Autowired
    private lateinit var bookRepository: JooqBookRepository

    @Nested
    @DisplayName("insert メソッド")
    inner class InsertTest {

        @Test
        @DisplayName("正常系：有効な書籍情報で挿入が成功する")
        fun insert_success() {
            // Given
            val book = createTestBook("テスト書籍", BigDecimal("1500.00"))

            // When
            bookRepository.insert(book)

            // Then
            val insertedBook = bookRepository.findById(book.id)
            assertThat(insertedBook).isNotNull()
            assertThat(insertedBook!!.id).isEqualTo(book.id)
            assertThat(insertedBook.title).isEqualTo(book.title)
            assertThat(insertedBook.bookPrice).isEqualTo(book.bookPrice)
            assertThat(insertedBook.publicationStatus).isEqualTo(book.publicationStatus)
        }

        @Test
        @DisplayName("境界値：各出版状況で挿入が成功する")
        fun insert_allPublicationStatuses() {
            // Given
            val publishedBook =
                createTestBookWithStatus("出版済み書籍", BigDecimal("1000"), PublicationStatus.PUBLISHED)
            val unpublishedBook =
                createTestBookWithStatus("未出版書籍", BigDecimal("2000"), PublicationStatus.UNPUBLISHED)

            // When
            bookRepository.insert(publishedBook)
            bookRepository.insert(unpublishedBook)

            // Then
            val insertedPublished = bookRepository.findById(publishedBook.id)
            val insertedUnpublished = bookRepository.findById(unpublishedBook.id)

            assertThat(insertedPublished).isNotNull()
            assertThat(insertedPublished!!.publicationStatus).isEqualTo(PublicationStatus.PUBLISHED)

            assertThat(insertedUnpublished).isNotNull()
            assertThat(insertedUnpublished!!.publicationStatus).isEqualTo(PublicationStatus.UNPUBLISHED)
        }
    }

    @Nested
    @DisplayName("findById メソッド")
    inner class FindByIdTest {

        @Test
        @DisplayName("正常系：存在する書籍IDで書籍情報と著者ID一覧が返される")
        @Sql("/sql/JooqBookRepository.sql")
        fun findById_exists() {
            // Given
            val bookId = BookId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))

            // When
            val foundBook = bookRepository.findById(bookId)

            // Then
            assertThat(foundBook).isNotNull()
            assertThat(foundBook!!.id).isEqualTo(bookId)
            assertThat(foundBook.title).isEqualTo(BookTitle("テスト書籍1"))
            assertThat(foundBook.bookPrice).isEqualTo(BookPrice(BigDecimal("1000.00")))
            assertThat(foundBook.authorIds).containsExactlyInAnyOrderElementsOf(
                listOf(
                    AuthorId(UUID.fromString("11111111-1111-1111-1111-111111111111")),
                    AuthorId(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                )
            )
        }

        @Test
        @DisplayName("正常系：存在しない書籍IDでnullが返される")
        fun findById_notExists() {
            // Given
            val id = BookId(UUID.randomUUID())

            // When
            val book = bookRepository.findById(id)

            // Then
            assertThat(book).isNull()
        }
    }

    @Nested
    @DisplayName("update メソッド")
    inner class UpdateTest {

        @Test
        @DisplayName("正常系：有効な書籍情報で更新が成功する")
        fun update_success() {
            // Given - 事前登録された書籍を使用
            val bookId = BookId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
            val insertedBook = bookRepository.findById(bookId)
            assertThat(insertedBook).isNotNull()
            val expectedUpdatedAt = insertedBook!!.updatedAt

            Thread.sleep(100) // updated_atの差分を作る

            val newTitle = BookTitle("新しいタイトル")
            val newPrice = BookPrice(BigDecimal("2000.00"))
            val newStatus = PublicationStatus.UNPUBLISHED

            // 更新用のBookオブジェクトを作成
            val updatedBook = Book(
                id = bookId,
                title = newTitle,
                bookPrice = newPrice,
                authorIds = insertedBook.authorIds, // 既存の著者IDを保持
                publicationStatus = newStatus,
                createdAt = insertedBook.createdAt,
                updatedAt = insertedBook.updatedAt
            )

            // When
            val updatedRows = bookRepository.update(updatedBook, expectedUpdatedAt)

            // Then
            assertThat(updatedRows).isEqualTo(1)
            val foundUpdatedBook = bookRepository.findById(bookId)
            assertThat(foundUpdatedBook).isNotNull()
            assertThat(foundUpdatedBook!!.title).isEqualTo(newTitle)
            assertThat(foundUpdatedBook.bookPrice).isEqualTo(newPrice)
            assertThat(foundUpdatedBook.publicationStatus).isEqualTo(newStatus)
        }

        @Test
        @DisplayName("異常系：存在しない書籍IDで更新時に0件更新される")
        fun update_nonExistentBook() {
            // Given
            val id = BookId(UUID.randomUUID())
            val title = BookTitle("テストタイトル変更")
            val price = BookPrice(BigDecimal("9999.99"))
            val status = PublicationStatus.PUBLISHED
            val book = Book(
                id,
                title,
                price,
                listOf(AuthorId(UUID.fromString("11111111-1111-1111-1111-111111111111"))),
                status,
                LocalDateTime.now(),
                LocalDateTime.now()
            )
            val updatedAt = LocalDateTime.now()

            // When
            val updatedRows = bookRepository.update(book, updatedAt)

            // Then
            assertThat(updatedRows).isEqualTo(0)
        }

        @Test
        @DisplayName("異常系：楽観排他制御で更新日時が異なる場合0件更新される")
        fun update_optimisticLockingFailure() {
            // Given - 事前登録された書籍を使用
            val bookId = BookId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"))
            val insertedBook = bookRepository.findById(bookId)
            assertThat(insertedBook).isNotNull()
            val wrongUpdatedAt = LocalDateTime.of(2020, 1, 1, 0, 0)

            // 更新用のBookオブジェクトを作成
            val updatedBook = Book(
                id = bookId,
                title = BookTitle("新しいタイトル"),
                bookPrice = BookPrice(BigDecimal("2000")),
                authorIds = insertedBook!!.authorIds,
                publicationStatus = PublicationStatus.UNPUBLISHED,
                createdAt = insertedBook.createdAt,
                updatedAt = insertedBook.updatedAt
            )

            // When
            val updatedRows = bookRepository.update(updatedBook, wrongUpdatedAt)

            // Then
            assertThat(updatedRows).isEqualTo(0)
        }
    }

    @Nested
    @DisplayName("findAllBookByAuthorId メソッド")
    inner class FindAllBookByAuthorIdTest {

        @Test
        @DisplayName("正常系：存在する著者IDで関連書籍一覧が返される")
        @Sql("/sql/JooqBookRepository.sql")
        fun findAllBookByAuthorId_exists() {
            // Given
            val authorId = AuthorId(UUID.fromString("11111111-1111-1111-1111-111111111111"))

            // When
            val foundBooks = bookRepository.findAllBookByAuthorId(authorId)

            // Then
            assertThat(foundBooks).hasSize(2)
            assertThat(foundBooks.map { it.id })
                .containsExactlyInAnyOrderElementsOf(
                    listOf(
                        BookId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")),
                        BookId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"))
                    )
                )
        }

        @Test
        @DisplayName("正常系：存在しない著者IDで空のリストが返される")
        fun findAllBookByAuthorId_notExists() {
            // Given
            val authorId = AuthorId(UUID.randomUUID())

            // When
            val foundBooks = bookRepository.findAllBookByAuthorId(authorId)

            // Then
            assertThat(foundBooks).isEmpty()
        }
    }

    @Nested
    @DisplayName("SQL発行確認テスト")
    inner class SqlExecutionTest {

        @Test
        @DisplayName("実際のSQLが発行されることを確認")
        fun verifySqlExecution() {
            // Given
            val book = createTestBook("SQLテスト書籍", BigDecimal("1500"))

            // When
            bookRepository.insert(book)

            // Then - 直接SQLで確認
            val count = dslContext.selectCount()
                .from(BOOKS)
                .where(BOOKS.ID.eq(book.id.value))
                .fetchOne(0, Int::class.java)

            assertThat(count).isEqualTo(1)

            // 実際のレコードを取得して確認
            val record = dslContext.selectFrom(BOOKS)
                .where(BOOKS.ID.eq(book.id.value))
                .fetchOne()

            assertThat(record).isNotNull()
            assertThat(record!!.getValue(BOOKS.TITLE)).isEqualTo("SQLテスト書籍")
            assertThat(record.getValue(BOOKS.PRICE)).isEqualTo(BigDecimal("1500.00"))
            assertThat(record.getValue(BOOKS.PUBLICATION_STATUS)).isEqualTo("PUBLISHED")
        }
    }

    // テストデータ作成用のヘルパーメソッド
    private fun createTestBook(title: String, price: BigDecimal): Book {
        return Book(
            id = BookId(UUID.randomUUID()),
            title = BookTitle(title),
            bookPrice = BookPrice(price),
            authorIds = listOf(AuthorId(UUID.fromString("11111111-1111-1111-1111-111111111111"))), // 最低1人の著者が必要
            publicationStatus = PublicationStatus.PUBLISHED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

    private fun createTestBookWithStatus(title: String, price: BigDecimal, status: PublicationStatus): Book {
        return Book(
            id = BookId(UUID.randomUUID()),
            title = BookTitle(title),
            bookPrice = BookPrice(price),
            authorIds = listOf(AuthorId(UUID.fromString("11111111-1111-1111-1111-111111111111"))), // 最低1人の著者が必要
            publicationStatus = status,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }
}
