package com.example.book_management.service

import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.book.*
import com.example.book_management.repository.BookRepository
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.dao.OptimisticLockingFailureException
import java.time.LocalDateTime
import java.util.*

@DisplayName("BookService 単体テスト")
class BookServiceTest {

    private val bookRepo: BookRepository = mockk()
    private val bookDomainService: BookDomainService = mockk()

    private val bookService = BookService(
        bookRepo = bookRepo,
        bookDomainService = bookDomainService
    )

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Nested
    @DisplayName("insert メソッド")
    inner class InsertTest {

        @Test
        @DisplayName("正常系：書籍の登録が成功する場合")
        fun insert_success() {
            // Given
            val book = createTestBook()

            every { bookRepo.insert(book) } just Runs

            // When
            bookService.insert(book)

            // Then
            verify { bookRepo.insert(book) }
        }
    }

    @Nested
    @DisplayName("update メソッド")
    inner class UpdateTest {

        @Test
        @DisplayName("正常系：書籍の更新が成功する場合")
        fun update_success() {
            // Given
            val book = createTestBook()
            val existingBook = mockk<Book>()

            every { bookDomainService.validateBookExists(book) } returns existingBook
            every { existingBook.publicationStatus } returns PublicationStatus.PUBLISHED
            every { existingBook.publicationStatus.transitionTo(book.publicationStatus) } returns book.publicationStatus
            every { existingBook.updatedAt } returns LocalDateTime.of(2024, 1, 2, 0, 0)
            every {
                bookRepo.update(
                    book,
                    existingBook.updatedAt
                )
            } returns 1

            // When
            bookService.update(book)

            // Then
            verify { bookDomainService.validateBookExists(book) }
            verify { existingBook.publicationStatus.transitionTo(book.publicationStatus) }
            verify {
                bookRepo.update(
                    book,
                    existingBook.updatedAt
                )
            }
        }

        @Test
        @DisplayName("異常系：書籍が存在しない場合")
        fun update_bookNotFound() {
            // Given
            val book = createTestBook()
            val errorMessage = "書籍が存在しません: ${book.id.value}"

            every { bookDomainService.validateBookExists(book) } throws IllegalArgumentException(errorMessage)

            // When & Then
            assertThatThrownBy { bookService.update(book) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(errorMessage)

            verify { bookDomainService.validateBookExists(book) }
            verify(exactly = 0) { bookRepo.update(any(), any()) }
        }

        @Test
        @DisplayName("異常系：無効な状態遷移の場合")
        fun update_invalidStatusTransition() {
            // Given
            val book = createTestBookWithStatus(PublicationStatus.UNPUBLISHED)
            val existingBook = mockk<Book>()

            every { bookDomainService.validateBookExists(book) } returns existingBook
            every { existingBook.publicationStatus } returns PublicationStatus.PUBLISHED
            every { existingBook.publicationStatus.transitionTo(book.publicationStatus) } throws IllegalStateException("無効な状態遷移です: PUBLISHED -> UNPUBLISHED")

            // When & Then
            assertThatThrownBy { bookService.update(book) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("無効な状態遷移です: PUBLISHED -> UNPUBLISHED")

            verify { bookDomainService.validateBookExists(book) }
            verify { existingBook.publicationStatus.transitionTo(book.publicationStatus) }
            verify(exactly = 0) { bookRepo.update(any(), any()) }
        }

        @Test
        @DisplayName("異常系：楽観排他制御エラーが発生する場合")
        fun update_optimisticLockingFailure() {
            // Given
            val book = createTestBook()
            val existingBook = mockk<Book>()

            every { bookDomainService.validateBookExists(book) } returns existingBook
            every { existingBook.publicationStatus } returns PublicationStatus.PUBLISHED
            every { existingBook.publicationStatus.transitionTo(book.publicationStatus) } returns book.publicationStatus
            every { existingBook.updatedAt } returns LocalDateTime.of(2024, 1, 2, 0, 0)
            every {
                bookRepo.update(
                    book,
                    existingBook.updatedAt
                )
            } returns 0

            // When & Then
            assertThatThrownBy { bookService.update(book) }
                .isInstanceOf(OptimisticLockingFailureException::class.java)
                .hasMessage("楽観排他制御エラー: 他のユーザーによって更新されています")

            verify { bookDomainService.validateBookExists(book) }
            verify { existingBook.publicationStatus.transitionTo(book.publicationStatus) }
            verify {
                bookRepo.update(
                    book,
                    existingBook.updatedAt
                )
            }
        }

        @Test
        @DisplayName("境界値：更新対象行が0件の場合")
        fun update_zeroRowsUpdated() {
            // Given
            val book = createTestBook()
            val existingBook = mockk<Book>()

            every { bookDomainService.validateBookExists(book) } returns existingBook
            every { existingBook.publicationStatus } returns PublicationStatus.PUBLISHED
            every { existingBook.publicationStatus.transitionTo(book.publicationStatus) } returns book.publicationStatus
            every { existingBook.updatedAt } returns LocalDateTime.of(2024, 1, 2, 0, 0)
            every {
                bookRepo.update(
                    book,
                    existingBook.updatedAt
                )
            } returns 0

            // When & Then
            assertThatThrownBy { bookService.update(book) }
                .isInstanceOf(OptimisticLockingFailureException::class.java)
                .hasMessage("楽観排他制御エラー: 他のユーザーによって更新されています")

            verify { bookDomainService.validateBookExists(book) }
            verify { existingBook.publicationStatus.transitionTo(book.publicationStatus) }
            verify {
                bookRepo.update(
                    book,
                    existingBook.updatedAt
                )
            }
        }
    }

    @Nested
    @DisplayName("findByAuthorId メソッド")
    inner class FindByAuthorIdTest {

        @Test
        @DisplayName("正常系：著者IDで書籍を検索する場合")
        fun findByAuthorId_success() {
            // Given
            val authorId = AuthorId(UUID.randomUUID())
            val expectedBooks = createMultipleTestBooks()

            every { bookRepo.findAllBookByAuthorId(authorId) } returns expectedBooks

            // When
            val result = bookService.findByAuthorId(authorId)

            // Then
            assertThat(result).isEqualTo(expectedBooks)
            verify { bookRepo.findAllBookByAuthorId(authorId) }
        }

        @Test
        @DisplayName("境界値：該当書籍が存在しない場合")
        fun findByAuthorId_noBooks() {
            // Given
            val authorId = AuthorId(UUID.randomUUID())
            val emptyBooks = emptyList<Book>()

            every { bookRepo.findAllBookByAuthorId(authorId) } returns emptyBooks

            // When
            val result = bookService.findByAuthorId(authorId)

            // Then
            assertThat(result).isEmpty()
            verify { bookRepo.findAllBookByAuthorId(authorId) }
        }
    }

    @Nested
    @DisplayName("統合テスト")
    inner class IntegrationTest {

        @Test
        @DisplayName("insert：トランザクションロールバックのテスト")
        fun insert_transactionRollback() {
            // Given
            val book = createTestBook()

            every { bookRepo.insert(book) } throws RuntimeException("データベースエラー")

            // When & Then
            assertThatThrownBy { bookService.insert(book) }
                .isInstanceOf(RuntimeException::class.java)
                .hasMessage("データベースエラー")

            verify { bookRepo.insert(book) }
        }

        @Test
        @DisplayName("update：更新時のトランザクションロールバック")
        fun update_transactionRollback() {
            // Given
            val book = createTestBook()
            val existingBook = mockk<Book>()

            every { bookDomainService.validateBookExists(book) } returns existingBook
            every { existingBook.publicationStatus } returns PublicationStatus.PUBLISHED
            every { existingBook.publicationStatus.transitionTo(book.publicationStatus) } returns book.publicationStatus
            every { existingBook.updatedAt } returns LocalDateTime.of(2024, 1, 2, 0, 0)
            every {
                bookRepo.update(
                    book,
                    existingBook.updatedAt
                )
            } throws RuntimeException("データベースエラー")

            // When & Then
            assertThatThrownBy { bookService.update(book) }
                .isInstanceOf(RuntimeException::class.java)
                .hasMessage("データベースエラー")

            verify { bookDomainService.validateBookExists(book) }
            verify { existingBook.publicationStatus.transitionTo(book.publicationStatus) }
            verify {
                bookRepo.update(
                    book,
                    existingBook.updatedAt
                )
            }
        }
    }

    // テストデータ作成用のヘルパーメソッド
    private fun createTestBook(): Book {
        return Book(
            id = BookId(UUID.randomUUID()),
            title = BookTitle("テスト書籍"),
            bookPrice = BookPrice(1000),
            authorIds = listOf(AuthorId(UUID.randomUUID())),
            publicationStatus = PublicationStatus.PUBLISHED,
            createdAt = LocalDateTime.of(2024, 1, 1, 0, 0),
            updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)
        )
    }

    private fun createTestBookWithStatus(status: PublicationStatus): Book {
        return Book(
            id = BookId(UUID.randomUUID()),
            title = BookTitle("テスト書籍"),
            bookPrice = BookPrice(1000),
            authorIds = listOf(AuthorId(UUID.randomUUID())),
            publicationStatus = status,
            createdAt = LocalDateTime.of(2024, 1, 1, 0, 0),
            updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)
        )
    }

    private fun createMultipleTestBooks(): List<Book> {
        val authorId = AuthorId(UUID.randomUUID())
        return listOf(
            Book(
                id = BookId(UUID.randomUUID()),
                title = BookTitle("テスト書籍1"),
                bookPrice = BookPrice(1000),
                authorIds = listOf(authorId),
                publicationStatus = PublicationStatus.PUBLISHED,
                createdAt = LocalDateTime.of(2024, 1, 1, 0, 0),
                updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            ),
            Book(
                id = BookId(UUID.randomUUID()),
                title = BookTitle("テスト書籍2"),
                bookPrice = BookPrice(2000),
                authorIds = listOf(authorId),
                publicationStatus = PublicationStatus.UNPUBLISHED,
                createdAt = LocalDateTime.of(2024, 1, 1, 0, 0),
                updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            )
        )
    }
}
