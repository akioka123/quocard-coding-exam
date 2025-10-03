package com.example.book_management.dto.author

import com.example.book_management.dto.book.BookPrice
import com.example.book_management.dto.book.BookResponse
import com.example.book_management.dto.book.BookTitle
import com.example.book_management.dto.book.PublicationStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

@DisplayName("AuthorResponse 単体テスト")
class AuthorResponseTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("有効な著者情報と本情報でオブジェクトが作成される")
        fun createAuthorResponse_validData() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val books = listOf(
                BookResponse(
                    id = UUID.randomUUID(),
                    authorIds = listOf(id),
                    title = BookTitle("テスト本1"),
                    bookPrice = BookPrice(1000),
                    publicationStatus = PublicationStatus.PUBLISHED
                )
            )

            // When
            val response = AuthorResponse(id, name, birthDate, books)

            // Then
            assertThat(response.id).isEqualTo(id)
            assertThat(response.name).isEqualTo(name)
            assertThat(response.birthDate).isEqualTo(birthDate)
            assertThat(response.books).isEqualTo(books)
            assertThat(response.books).hasSize(1)
        }

        @Test
        @DisplayName("本情報が空リストでオブジェクトが作成される")
        fun createAuthorResponse_emptyBooks() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val books = emptyList<BookResponse>()

            // When
            val response = AuthorResponse(id, name, birthDate, books)

            // Then
            assertThat(response.id).isEqualTo(id)
            assertThat(response.name).isEqualTo(name)
            assertThat(response.birthDate).isEqualTo(birthDate)
            assertThat(response.books).isEmpty()
        }

        @Test
        @DisplayName("複数の本情報でオブジェクトが作成される")
        fun createAuthorResponse_multipleBooks() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val authorId = AuthorId(UUID.randomUUID())
            val books = listOf(
                BookResponse(
                    id = UUID.randomUUID(),
                    authorIds = listOf(authorId),
                    title = BookTitle("テスト本1"),
                    bookPrice = BookPrice(1000),
                    publicationStatus = PublicationStatus.PUBLISHED
                ),
                BookResponse(
                    id = UUID.randomUUID(),
                    authorIds = listOf(authorId),
                    title = BookTitle("テスト本2"),
                    bookPrice = BookPrice(2000),
                    publicationStatus = PublicationStatus.UNPUBLISHED
                )
            )

            // When
            val response = AuthorResponse(id, name, birthDate, books)

            // Then
            assertThat(response.id).isEqualTo(id)
            assertThat(response.name).isEqualTo(name)
            assertThat(response.birthDate).isEqualTo(birthDate)
            assertThat(response.books).hasSize(2)
            assertThat(response.books[0].title.value).isEqualTo("テスト本1")
            assertThat(response.books[1].title.value).isEqualTo("テスト本2")
        }
    }

    @Nested
    @DisplayName("等価性テスト")
    inner class EqualityTest {

        @Test
        @DisplayName("同じ値のAuthorResponseは等価である")
        fun authorResponse_equality_sameValue() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val authorId = AuthorId(UUID.randomUUID())
            val books = listOf(
                BookResponse(
                    id = UUID.randomUUID(),
                    authorIds = listOf(authorId),
                    title = BookTitle("テスト本"),
                    bookPrice = BookPrice(1000),
                    publicationStatus = PublicationStatus.PUBLISHED
                )
            )

            val response1 = AuthorResponse(id, name, birthDate, books)
            val response2 = AuthorResponse(id, name, birthDate, books)

            // When & Then
            assertThat(response1).isEqualTo(response2)
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode())
        }

        @Test
        @DisplayName("異なる値のAuthorResponseは等価でない")
        fun authorResponse_equality_differentValue() {
            // Given
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val books = emptyList<BookResponse>()

            val response1 = AuthorResponse(AuthorId(UUID.randomUUID()), name, birthDate, books)
            val response2 = AuthorResponse(AuthorId(UUID.randomUUID()), name, birthDate, books)

            // When & Then
            assertThat(response1).isNotEqualTo(response2)
        }

        @Test
        @DisplayName("異なる著者名のAuthorResponseは等価でない")
        fun authorResponse_equality_differentName() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name1 = AuthorName("テスト著者1")
            val name2 = AuthorName("テスト著者2")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val books = emptyList<BookResponse>()

            val response1 = AuthorResponse(id, name1, birthDate, books)
            val response2 = AuthorResponse(id, name2, birthDate, books)

            // When & Then
            assertThat(response1).isNotEqualTo(response2)
        }
    }

    @Nested
    @DisplayName("コピーテスト")
    inner class CopyTest {

        @Test
        @DisplayName("copy()で新しいオブジェクトが作成される")
        fun authorResponse_copy() {
            // Given
            val originalId = AuthorId(UUID.randomUUID())
            val originalName = AuthorName("テスト著者")
            val originalBirthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val originalBooks = emptyList<BookResponse>()
            val originalResponse = AuthorResponse(originalId, originalName, originalBirthDate, originalBooks)

            val newName = AuthorName("新しい著者")

            // When
            val copiedResponse = originalResponse.copy(name = newName)

            // Then
            assertThat(copiedResponse.id).isEqualTo(originalId)
            assertThat(copiedResponse.name).isEqualTo(newName)
            assertThat(copiedResponse.birthDate).isEqualTo(originalBirthDate)
            assertThat(copiedResponse.books).isEqualTo(originalBooks)
            assertThat(copiedResponse).isNotEqualTo(originalResponse)
        }

        @Test
        @DisplayName("copy()でIDを変更した新しいオブジェクトが作成される")
        fun authorResponse_copyId() {
            // Given
            val originalId = AuthorId(UUID.randomUUID())
            val originalName = AuthorName("テスト著者")
            val originalBirthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val originalBooks = emptyList<BookResponse>()
            val originalResponse = AuthorResponse(originalId, originalName, originalBirthDate, originalBooks)

            val newId = AuthorId(UUID.randomUUID())

            // When
            val copiedResponse = originalResponse.copy(id = newId)

            // Then
            assertThat(copiedResponse.id).isEqualTo(newId)
            assertThat(copiedResponse.name).isEqualTo(originalName)
            assertThat(copiedResponse.birthDate).isEqualTo(originalBirthDate)
            assertThat(copiedResponse.books).isEqualTo(originalBooks)
            assertThat(copiedResponse).isNotEqualTo(originalResponse)
        }
    }
}
