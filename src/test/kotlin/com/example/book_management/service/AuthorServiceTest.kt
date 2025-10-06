package com.example.book_management.service

import com.example.book_management.dto.author.Author
import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.author.AuthorName
import com.example.book_management.dto.author.BirthDate
import com.example.book_management.dto.book.*
import com.example.book_management.repository.AuthorRepository
import io.mockk.*
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.dao.OptimisticLockingFailureException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@DisplayName("AuthorService 単体テスト")
class AuthorServiceTest {

    private val authorRepo: AuthorRepository = mockk()
    private val authorDomainService: AuthorDomainService = mockk()

    private val authorService = AuthorService(
        authorRepo = authorRepo,
        authorDomainService = authorDomainService
    )

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Nested
    @DisplayName("insert メソッド")
    inner class InsertTest {

        @Test
        @DisplayName("正常系：著者の登録が成功する場合")
        fun insert_success() {
            // Given
            val author = createTestAuthor()
            val books = createTestBooks()

            every { authorRepo.insert(author, books.map { it.id }) } just Runs

            // When
            authorService.insert(author, books.map { it.id })

            // Then
            verify { authorRepo.insert(author, books.map { it.id }) }
        }

        @Test
        @DisplayName("境界値：書籍リストが空の場合")
        fun insert_emptyBooksList() {
            // Given
            val author = createTestAuthor()
            val emptyBooks = emptyList<Book>()

            every { authorRepo.insert(author, emptyBooks.map { it.id }) } just Runs

            // When
            authorService.insert(author, emptyBooks.map { it.id })

            // Then
            verify { authorRepo.insert(author, emptyBooks.map { it.id }) }
        }

        @Test
        @DisplayName("境界値：複数の書籍が登録される場合")
        fun insert_multipleBooks() {
            // Given
            val author = createTestAuthor()
            val multipleBooks = createMultipleTestBooks()

            every { authorRepo.insert(author, multipleBooks.map { it.id }) } just Runs

            // When
            authorService.insert(author, multipleBooks.map { it.id })

            // Then
            verify { authorRepo.insert(author, multipleBooks.map { it.id }) }
        }

        @Test
        @DisplayName("境界値：重複書籍が含まれる場合")
        fun insert_withDuplicateBooks() {
            // Given
            val author = createTestAuthor()
            val duplicateBook = createTestBooks()[0]
            val notDuplicateBook = Book(
                id = BookId(UUID.randomUUID()),
                title = BookTitle("テスト書籍2"),
                bookPrice = BookPrice(2000),
                authorIds = listOf(AuthorId(UUID.randomUUID())),
                publicationStatus = PublicationStatus.UNPUBLISHED,
                createdAt = LocalDateTime.of(2024, 1, 1, 0, 0),
                updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            )
            val allBooks = listOf(duplicateBook, notDuplicateBook)

            every { authorRepo.insert(author, allBooks.map { it.id }) } just Runs

            // When
            authorService.insert(author, allBooks.map { it.id })

            // Then
            verify { authorRepo.insert(author, allBooks.map { it.id }) }
        }
    }

    @Nested
    @DisplayName("update メソッド")
    inner class UpdateTest {

        @Test
        @DisplayName("正常系：著者の更新が成功する場合")
        fun update_success() {
            // Given
            val author = createTestAuthor()
            val existingAuthor = createTestAuthorWithUpdatedAt()
            val bookIds = createTestBooks().map { it.id }

            every { authorDomainService.validateAuthorExists(author) } returns existingAuthor
            every { 
                authorRepo.update(
                    author.id, 
                    author.name, 
                    author.birthDate, 
                    bookIds,
                    existingAuthor.updatedAt
                ) 
            } returns 1

            // When
            authorService.update(author, bookIds)

            // Then
            verify { authorDomainService.validateAuthorExists(author) }
            verify { 
                authorRepo.update(
                    author.id, 
                    author.name, 
                    author.birthDate, 
                    bookIds,
                    existingAuthor.updatedAt
                ) 
            }
        }

        @Test
        @DisplayName("異常系：著者が存在しない場合")
        fun update_authorNotFound() {
            // Given
            val author = createTestAuthor()
            val bookIds = createTestBooks().map { it.id }
            val errorMessage = "著者が存在しません: ${author.id.value}"

            every { authorDomainService.validateAuthorExists(author) } throws IllegalArgumentException(errorMessage)

            // When & Then
            assertThatThrownBy { authorService.update(author, bookIds) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(errorMessage)

            verify { authorDomainService.validateAuthorExists(author) }
            verify(exactly = 0) { authorRepo.update(any(), any(), any(), any(), any()) }
        }

        @Test
        @DisplayName("異常系：楽観排他制御エラーが発生する場合")
        fun update_optimisticLockingFailure() {
            // Given
            val author = createTestAuthor()
            val existingAuthor = createTestAuthorWithUpdatedAt()
            val bookIds = createTestBooks().map { it.id }

            every { authorDomainService.validateAuthorExists(author) } returns existingAuthor
            every { 
                authorRepo.update(
                    author.id, 
                    author.name, 
                    author.birthDate, 
                    bookIds,
                    existingAuthor.updatedAt
                ) 
            } returns 0

            // When & Then
            assertThatThrownBy { authorService.update(author, bookIds) }
                .isInstanceOf(OptimisticLockingFailureException::class.java)
                .hasMessage("楽観排他制御エラー: 他のユーザーによって更新されています")

            verify { authorDomainService.validateAuthorExists(author) }
            verify { 
                authorRepo.update(
                    author.id, 
                    author.name, 
                    author.birthDate, 
                    bookIds,
                    existingAuthor.updatedAt
                ) 
            }
        }

        @Test
        @DisplayName("境界値：更新対象行が0件の場合")
        fun update_zeroRowsUpdated() {
            // Given
            val author = createTestAuthor()
            val existingAuthor = createTestAuthorWithUpdatedAt()
            val bookIds = createTestBooks().map { it.id }

            every { authorDomainService.validateAuthorExists(author) } returns existingAuthor
            every { 
                authorRepo.update(
                    author.id, 
                    author.name, 
                    author.birthDate, 
                    bookIds,
                    existingAuthor.updatedAt
                ) 
            } returns 0

            // When & Then
            assertThatThrownBy { authorService.update(author, bookIds) }
                .isInstanceOf(OptimisticLockingFailureException::class.java)
                .hasMessage("楽観排他制御エラー: 他のユーザーによって更新されています")

            verify { authorDomainService.validateAuthorExists(author) }
            verify { 
                authorRepo.update(
                    author.id, 
                    author.name, 
                    author.birthDate, 
                    bookIds,
                    existingAuthor.updatedAt
                ) 
            }
        }

        @Test
        @DisplayName("境界値：書籍リストが空の場合")
        fun update_emptyBookIds() {
            // Given
            val author = createTestAuthor()
            val existingAuthor = createTestAuthorWithUpdatedAt()
            val emptyBookIds = emptyList<BookId>()

            every { authorDomainService.validateAuthorExists(author) } returns existingAuthor
            every { 
                authorRepo.update(
                    author.id, 
                    author.name, 
                    author.birthDate, 
                    emptyBookIds,
                    existingAuthor.updatedAt
                ) 
            } returns 1

            // When
            authorService.update(author, emptyBookIds)

            // Then
            verify { authorDomainService.validateAuthorExists(author) }
            verify { 
                authorRepo.update(
                    author.id, 
                    author.name, 
                    author.birthDate, 
                    emptyBookIds,
                    existingAuthor.updatedAt
                ) 
            }
        }

        @Test
        @DisplayName("境界値：複数の書籍IDが指定される場合")
        fun update_multipleBookIds() {
            // Given
            val author = createTestAuthor()
            val existingAuthor = createTestAuthorWithUpdatedAt()
            val multipleBookIds = createMultipleTestBooks().map { it.id }

            every { authorDomainService.validateAuthorExists(author) } returns existingAuthor
            every { 
                authorRepo.update(
                    author.id, 
                    author.name, 
                    author.birthDate, 
                    multipleBookIds,
                    existingAuthor.updatedAt
                ) 
            } returns 1

            // When
            authorService.update(author, multipleBookIds)

            // Then
            verify { authorDomainService.validateAuthorExists(author) }
            verify { 
                authorRepo.update(
                    author.id, 
                    author.name, 
                    author.birthDate, 
                    multipleBookIds,
                    existingAuthor.updatedAt
                ) 
            }
        }
    }

    @Nested
    @DisplayName("統合テスト")
    inner class IntegrationTest {

        @Test
        @DisplayName("insert：トランザクションロールバックのテスト")
        fun insert_transactionRollback() {
            // Given
            val author = createTestAuthor()
            val books = createTestBooks()

            every { authorRepo.insert(author, books.map { it.id }) } throws RuntimeException("データベースエラー")

            // When & Then
            assertThatThrownBy { authorService.insert(author, books.map { it.id }) }
                .isInstanceOf(RuntimeException::class.java)
                .hasMessage("データベースエラー")

            verify { authorRepo.insert(author, books.map { it.id }) }
        }

        @Test
        @DisplayName("update：更新時のトランザクションロールバック")
        fun update_transactionRollback() {
            // Given
            val author = createTestAuthor()
            val existingAuthor = createTestAuthorWithUpdatedAt()
            val bookIds = createTestBooks().map { it.id }

            every { authorDomainService.validateAuthorExists(author) } returns existingAuthor
            every {
                authorRepo.update(
                    author.id,
                    author.name,
                    author.birthDate,
                    bookIds,
                    existingAuthor.updatedAt
                )
            } throws RuntimeException("データベースエラー")

            // When & Then
            assertThatThrownBy { authorService.update(author, bookIds) }
                .isInstanceOf(RuntimeException::class.java)
                .hasMessage("データベースエラー")

            verify { authorDomainService.validateAuthorExists(author) }
            verify { 
                authorRepo.update(
                    author.id,
                    author.name,
                    author.birthDate,
                    bookIds,
                    existingAuthor.updatedAt
                ) 
            }
        }
    }

    // テストデータ作成用のヘルパーメソッド
    private fun createTestAuthor(): Author {
        return Author(
            id = AuthorId(UUID.randomUUID()),
            name = AuthorName("テスト著者"),
            birthDate = BirthDate(LocalDate.of(1990, 1, 1)),
            createdAt = LocalDateTime.of(2024, 1, 1, 0, 0),
            updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)
        )
    }

    private fun createTestAuthorWithUpdatedAt(): Author {
        return Author(
            id = AuthorId(UUID.randomUUID()),
            name = AuthorName("テスト著者"),
            birthDate = BirthDate(LocalDate.of(1990, 1, 1)),
            createdAt = LocalDateTime.of(2024, 1, 1, 0, 0),
            updatedAt = LocalDateTime.of(2024, 1, 2, 0, 0)
        )
    }

    private fun createTestBooks(): List<Book> {
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
            )
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
            ),
            Book(
                id = BookId(UUID.randomUUID()),
                title = BookTitle("テスト書籍3"),
                bookPrice = BookPrice(3000),
                authorIds = listOf(authorId),
                publicationStatus = PublicationStatus.PUBLISHED,
                createdAt = LocalDateTime.of(2024, 1, 1, 0, 0),
                updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            )
        )
    }
}

