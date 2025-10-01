package com.example.book_management.service

import com.example.book_management.dto.author.Author
import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.author.AuthorName
import com.example.book_management.dto.author.BirthDate
import com.example.book_management.dto.book.*
import com.example.book_management.repository.AuthorRepository
import com.example.book_management.repository.BookAuthorsRepository
import com.example.book_management.repository.BookRepository
import io.mockk.*
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.OptimisticLockingFailureException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@DisplayName("AuthorService 単体テスト")
class AuthorServiceTest {

    private val authorRepo: AuthorRepository = mockk()
    private val bookRepo: BookRepository = mockk()
    private val bookAuthorsRepo: BookAuthorsRepository = mockk()
    private val authorDomainService: AuthorDomainService = mockk()
    private val bookDomainService: BookDomainService = mockk()

    private val authorService = AuthorService(
        authorRepo = authorRepo,
        bookRepo = bookRepo,
        bookAuthorsRepo = bookAuthorsRepo,
        authorDomainService = authorDomainService,
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
        @DisplayName("正常系：著者と書籍の登録が成功する場合")
        fun insert_success() {
            // Given
            val author = createTestAuthor()
            val books = createTestBooks()

            every { authorDomainService.isDuplicateAuthor(author.name, author.birthDate) } returns false
            every { bookDomainService.isDuplicateBook(any()) } returns false
            every { authorRepo.insert(author.id, author.name, author.birthDate) } just Runs
            every { bookRepo.insertMany(books) } just Runs
            every { bookAuthorsRepo.insertAuthorBooks(author.id, books.map { it.id }) } just Runs

            // When
            authorService.insert(author, books)

            // Then
            verify { authorDomainService.isDuplicateAuthor(author.name, author.birthDate) }
            verify { bookDomainService.isDuplicateBook(any()) }
            verify { authorRepo.insert(author.id, author.name, author.birthDate) }
            verify { bookRepo.insertMany(books) }
            verify { bookAuthorsRepo.insertAuthorBooks(author.id, books.map { it.id }) }
        }

        @Test
        @DisplayName("異常系：重複著者が存在する場合")
        fun insert_duplicateAuthor() {
            // Given
            val author = createTestAuthor()
            val books = createTestBooks()

            every { authorDomainService.isDuplicateAuthor(author.name, author.birthDate) } returns true

            // When & Then
            assertThatThrownBy { authorService.insert(author, books) }
                .isInstanceOf(DuplicateKeyException::class.java)
                .hasMessage("同名で生年月日が同じ著者が存在します。")

            verify { authorDomainService.isDuplicateAuthor(author.name, author.birthDate) }
            verify(exactly = 0) { authorRepo.insert(any(), any(), any()) }
            verify(exactly = 0) { bookRepo.insertMany(any()) }
            verify(exactly = 0) { bookAuthorsRepo.insertAuthorBooks(any(), any()) }
        }

        @Test
        @DisplayName("境界値：書籍リストが空の場合")
        fun insert_emptyBooksList() {
            // Given
            val author = createTestAuthor()
            val emptyBooks = emptyList<Book>()

            every { authorDomainService.isDuplicateAuthor(author.name, author.birthDate) } returns false
            every { bookDomainService.isDuplicateBook(any()) } returns false
            every { authorRepo.insert(author.id, author.name, author.birthDate) } just Runs
            every { bookRepo.insertMany(emptyBooks) } just Runs
            every { bookAuthorsRepo.insertAuthorBooks(author.id, emptyBooks.map { it.id }) } just Runs

            // When
            authorService.insert(author, emptyBooks)

            // Then
            verify { authorDomainService.isDuplicateAuthor(author.name, author.birthDate) }
            verify(exactly = 0) { bookDomainService.isDuplicateBook(any()) }
            verify { authorRepo.insert(author.id, author.name, author.birthDate) }
            verify { bookRepo.insertMany(emptyBooks) }
            verify { bookAuthorsRepo.insertAuthorBooks(author.id, emptyBooks.map { it.id }) }
        }

        @Test
        @DisplayName("境界値：複数の書籍が登録される場合")
        fun insert_multipleBooks() {
            // Given
            val author = createTestAuthor()
            val multipleBooks = createMultipleTestBooks()

            every { authorDomainService.isDuplicateAuthor(author.name, author.birthDate) } returns false
            every { bookDomainService.isDuplicateBook(any()) } returns false
            every { authorRepo.insert(author.id, author.name, author.birthDate) } just Runs
            every { bookRepo.insertMany(multipleBooks) } just Runs
            every { bookAuthorsRepo.insertAuthorBooks(author.id, multipleBooks.map { it.id }) } just Runs

            // When
            authorService.insert(author, multipleBooks)

            // Then
            verify { authorDomainService.isDuplicateAuthor(author.name, author.birthDate) }
            verify { bookDomainService.isDuplicateBook(any()) }
            verify { authorRepo.insert(author.id, author.name, author.birthDate) }
            verify { bookRepo.insertMany(multipleBooks) }
            verify { bookAuthorsRepo.insertAuthorBooks(author.id, multipleBooks.map { it.id }) }
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
                authors = listOf(AuthorId(UUID.randomUUID())),
                publicationStatus = PublicationStatus.UNPUBLISHED,
                createdAt = LocalDateTime.of(2024, 1, 1, 0, 0),
                updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            )
            val allBooks = listOf(duplicateBook, notDuplicateBook)

            every { authorDomainService.isDuplicateAuthor(author.name, author.birthDate) } returns false
            every { bookDomainService.isDuplicateBook(duplicateBook) } returns true
            every { bookDomainService.isDuplicateBook(notDuplicateBook) } returns false
            every { authorRepo.insert(author.id, author.name, author.birthDate) } just Runs
            every { bookRepo.insertMany(listOf(notDuplicateBook)) } just Runs
            every { bookAuthorsRepo.insertAuthorBooks(author.id, allBooks.map { it.id }) } just Runs

            // When
            authorService.insert(author, allBooks)

            // Then
            verify { authorDomainService.isDuplicateAuthor(author.name, author.birthDate) }
            verify { bookDomainService.isDuplicateBook(duplicateBook) }
            verify { bookDomainService.isDuplicateBook(notDuplicateBook) }
            verify { authorRepo.insert(author.id, author.name, author.birthDate) }
            verify { bookRepo.insertMany(listOf(notDuplicateBook)) } // 重複しない書籍のみ登録
            verify { bookAuthorsRepo.insertAuthorBooks(author.id, allBooks.map { it.id }) } // 全ての書籍の関連付け
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

            every { authorDomainService.validateAuthorExists(author) } returns existingAuthor
            every { authorRepo.update(author.id, author.name, author.birthDate, existingAuthor.updatedAt) } returns 1

            // When
            authorService.update(author)

            // Then
            verify { authorDomainService.validateAuthorExists(author) }
            verify { authorRepo.update(author.id, author.name, author.birthDate, existingAuthor.updatedAt) }
        }

        @Test
        @DisplayName("異常系：著者が存在しない場合")
        fun update_authorNotFound() {
            // Given
            val author = createTestAuthor()
            val errorMessage = "著者が存在しません: ${author.id.value}"

            every { authorDomainService.validateAuthorExists(author) } throws IllegalArgumentException(errorMessage)

            // When & Then
            assertThatThrownBy { authorService.update(author) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(errorMessage)

            verify { authorDomainService.validateAuthorExists(author) }
            verify(exactly = 0) { authorRepo.update(any(), any(), any(), any()) }
        }

        @Test
        @DisplayName("異常系：楽観排他制御エラーが発生する場合")
        fun update_optimisticLockingFailure() {
            // Given
            val author = createTestAuthor()
            val existingAuthor = createTestAuthorWithUpdatedAt()

            every { authorDomainService.validateAuthorExists(author) } returns existingAuthor
            every { authorRepo.update(author.id, author.name, author.birthDate, existingAuthor.updatedAt) } returns 0

            // When & Then
            assertThatThrownBy { authorService.update(author) }
                .isInstanceOf(OptimisticLockingFailureException::class.java)
                .hasMessage("楽観排他制御エラー: 他のユーザーによって更新されています")

            verify { authorDomainService.validateAuthorExists(author) }
            verify { authorRepo.update(author.id, author.name, author.birthDate, existingAuthor.updatedAt) }
        }

        @Test
        @DisplayName("境界値：更新対象行が0件の場合")
        fun update_zeroRowsUpdated() {
            // Given
            val author = createTestAuthor()
            val existingAuthor = createTestAuthorWithUpdatedAt()

            every { authorDomainService.validateAuthorExists(author) } returns existingAuthor
            every { authorRepo.update(author.id, author.name, author.birthDate, existingAuthor.updatedAt) } returns 0

            // When & Then
            assertThatThrownBy { authorService.update(author) }
                .isInstanceOf(OptimisticLockingFailureException::class.java)
                .hasMessage("楽観排他制御エラー: 他のユーザーによって更新されています")

            verify { authorDomainService.validateAuthorExists(author) }
            verify { authorRepo.update(author.id, author.name, author.birthDate, existingAuthor.updatedAt) }
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

            every { authorDomainService.isDuplicateAuthor(author.name, author.birthDate) } returns false
            every { bookDomainService.isDuplicateBook(any()) } returns false
            every { authorRepo.insert(author.id, author.name, author.birthDate) } just Runs
            every { bookRepo.insertMany(books) } throws RuntimeException("データベースエラー")
            every { bookAuthorsRepo.insertAuthorBooks(author.id, books.map { it.id }) } just Runs

            // When & Then
            assertThatThrownBy { authorService.insert(author, books) }
                .isInstanceOf(RuntimeException::class.java)
                .hasMessage("データベースエラー")

            verify { authorDomainService.isDuplicateAuthor(author.name, author.birthDate) }
            verify { bookDomainService.isDuplicateBook(any()) }
            verify { authorRepo.insert(author.id, author.name, author.birthDate) }
            verify { bookRepo.insertMany(books) }
            // bookAuthorsRepo.insertAuthorBooksは呼ばれない（トランザクションロールバック）
            verify(exactly = 0) { bookAuthorsRepo.insertAuthorBooks(any(), any()) }
        }

        @Test
        @DisplayName("update：更新時のトランザクションロールバック")
        fun update_transactionRollback() {
            // Given
            val author = createTestAuthor()
            val existingAuthor = createTestAuthorWithUpdatedAt()

            every { authorDomainService.validateAuthorExists(author) } returns existingAuthor
            every {
                authorRepo.update(
                    author.id,
                    author.name,
                    author.birthDate,
                    existingAuthor.updatedAt
                )
            } throws RuntimeException("データベースエラー")

            // When & Then
            assertThatThrownBy { authorService.update(author) }
                .isInstanceOf(RuntimeException::class.java)
                .hasMessage("データベースエラー")

            verify { authorDomainService.validateAuthorExists(author) }
            verify { authorRepo.update(author.id, author.name, author.birthDate, existingAuthor.updatedAt) }
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
                authors = listOf(authorId),
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
                authors = listOf(authorId),
                publicationStatus = PublicationStatus.PUBLISHED,
                createdAt = LocalDateTime.of(2024, 1, 1, 0, 0),
                updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            ),
            Book(
                id = BookId(UUID.randomUUID()),
                title = BookTitle("テスト書籍2"),
                bookPrice = BookPrice(2000),
                authors = listOf(authorId),
                publicationStatus = PublicationStatus.UNPUBLISHED,
                createdAt = LocalDateTime.of(2024, 1, 1, 0, 0),
                updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            ),
            Book(
                id = BookId(UUID.randomUUID()),
                title = BookTitle("テスト書籍3"),
                bookPrice = BookPrice(3000),
                authors = listOf(authorId),
                publicationStatus = PublicationStatus.PUBLISHED,
                createdAt = LocalDateTime.of(2024, 1, 1, 0, 0),
                updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            )
        )
    }
}

