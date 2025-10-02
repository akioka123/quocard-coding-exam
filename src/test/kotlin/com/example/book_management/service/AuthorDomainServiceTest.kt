package com.example.book_management.service

import com.example.book_management.dto.author.Author
import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.author.AuthorName
import com.example.book_management.dto.author.BirthDate
import com.example.book_management.repository.AuthorRepository
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@DisplayName("AuthorDomainService 単体テスト")
class AuthorDomainServiceTest {

    private val authorRepository: AuthorRepository = mockk()
    private val authorDomainService = AuthorDomainService(authorRepository)

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Nested
    @DisplayName("isDuplicateAuthor メソッド")
    inner class IsDuplicateAuthorTest {

        @Test
        @DisplayName("正常系：重複著者が存在しない場合")
        fun isDuplicateAuthor_noDuplicate() {
            // Given
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))

            every { authorRepository.existsByNameAndBirthDate(name, birthDate) } returns false

            // When
            val result = authorDomainService.isDuplicateAuthor(name, birthDate)

            // Then
            assertThat(result).isFalse()
            verify { authorRepository.existsByNameAndBirthDate(name, birthDate) }
        }

        @Test
        @DisplayName("正常系：重複著者が存在する場合")
        fun isDuplicateAuthor_duplicateExists() {
            // Given
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))

            every { authorRepository.existsByNameAndBirthDate(name, birthDate) } returns true

            // When
            val result = authorDomainService.isDuplicateAuthor(name, birthDate)

            // Then
            assertThat(result).isTrue()
            verify { authorRepository.existsByNameAndBirthDate(name, birthDate) }
        }
    }

    @Nested
    @DisplayName("validateAuthorExists メソッド")
    inner class ValidateAuthorExistsTest {

        @Test
        @DisplayName("正常系：著者が存在する場合")
        fun validateAuthorExists_authorExists() {
            // Given
            val author = createTestAuthor()
            val existingAuthor = createTestAuthorWithUpdatedAt()

            every { authorRepository.findById(author.id) } returns existingAuthor

            // When
            val result = authorDomainService.validateAuthorExists(author)

            // Then
            assertThat(result).isEqualTo(existingAuthor)
            verify { authorRepository.findById(author.id) }
        }

        @Test
        @DisplayName("異常系：著者が存在しない場合")
        fun validateAuthorExists_authorNotFound() {
            // Given
            val author = createTestAuthor()
            val expectedMessage = "著者が存在しません: ${author.id.value}"

            every { authorRepository.findById(author.id) } returns null

            // When & Then
            assertThatThrownBy { authorDomainService.validateAuthorExists(author) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(expectedMessage)

            verify { authorRepository.findById(author.id) }
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
}

