package com.example.book_management.service

import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.book.*
import com.example.book_management.repository.BookRepository
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

@DisplayName("BookDomainService 単体テスト")
class BookDomainServiceTest {

    private val bookRepository: BookRepository = mockk()
    private val bookDomainService = BookDomainService(bookRepository)

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Nested
    @DisplayName("isDuplicateBook メソッド")
    inner class IsDuplicateBookTest {

        @Test
        @DisplayName("正常系：重複書籍が存在しない場合")
        fun isDuplicateBook_noDuplicate() {
            // Given
            val book = createTestBook()

            every { bookRepository.existsByTitleAndPrice(book) } returns false

            // When
            val result = bookDomainService.isDuplicateBook(book)

            // Then
            assertThat(result).isFalse()
            verify { bookRepository.existsByTitleAndPrice(book) }
        }

        @Test
        @DisplayName("正常系：重複書籍が存在する場合")
        fun isDuplicateBook_duplicateExists() {
            // Given
            val book = createTestBook()

            every { bookRepository.existsByTitleAndPrice(book) } returns true

            // When
            val result = bookDomainService.isDuplicateBook(book)

            // Then
            assertThat(result).isTrue()
            verify { bookRepository.existsByTitleAndPrice(book) }
        }
    }

    @Nested
    @DisplayName("validateBookExists メソッド")
    inner class ValidateBookExistsTest {

        @Test
        @DisplayName("正常系：書籍が存在する場合")
        fun validateBookExists_bookExists() {
            // Given
            val book = createTestBook()
            val existingBook = createTestBookWithUpdatedAt()

            every { bookRepository.findById(book.id) } returns existingBook

            // When
            val result = bookDomainService.validateBookExists(book)

            // Then
            assertThat(result).isEqualTo(existingBook)
            verify { bookRepository.findById(book.id) }
        }

        @Test
        @DisplayName("異常系：書籍が存在しない場合")
        fun validateBookExists_bookNotFound() {
            // Given
            val book = createTestBook()
            val expectedMessage = "書籍が存在しません: ${book.id.value}"

            every { bookRepository.findById(book.id) } returns null

            // When & Then
            assertThatThrownBy { bookDomainService.validateBookExists(book) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(expectedMessage)

            verify { bookRepository.findById(book.id) }
        }
    }

    // テストデータ作成用のヘルパーメソッド
    private fun createTestBook(): Book {
        return Book(
            id = BookId(UUID.randomUUID()),
            title = BookTitle("テスト書籍"),
            bookPrice = BookPrice(1000),
            authors = listOf(AuthorId(UUID.randomUUID())),
            publicationStatus = PublicationStatus.PUBLISHED,
            createdAt = LocalDateTime.of(2024, 1, 1, 0, 0),
            updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)
        )
    }

    private fun createTestBookWithUpdatedAt(): Book {
        return Book(
            id = BookId(UUID.randomUUID()),
            title = BookTitle("テスト書籍"),
            bookPrice = BookPrice(1000),
            authors = listOf(AuthorId(UUID.randomUUID())),
            publicationStatus = PublicationStatus.PUBLISHED,
            createdAt = LocalDateTime.of(2024, 1, 1, 0, 0),
            updatedAt = LocalDateTime.of(2024, 1, 2, 0, 0)
        )
    }

}

