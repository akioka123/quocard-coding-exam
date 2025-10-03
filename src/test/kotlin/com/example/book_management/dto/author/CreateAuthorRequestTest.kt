package com.example.book_management.dto.author

import com.example.book_management.dto.book.BookPrice
import com.example.book_management.dto.book.BookTitle
import com.example.book_management.dto.book.CreateBookWithAuthorRequest
import com.example.book_management.dto.book.PublicationStatus
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

@DisplayName("CreateAuthorRequest 単体テスト")
class CreateAuthorRequestTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("有効な著者情報と本情報でオブジェクトが作成される")
        fun createAuthorRequest_validData() {
            // Given
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val books = listOf(
                CreateBookWithAuthorRequest(
                    title = BookTitle("テスト本1"),
                    bookPrice = BookPrice(1000),
                    publicationStatus = PublicationStatus.PUBLISHED
                )
            )

            // When
            val request = CreateAuthorRequest(name, birthDate, books)

            // Then
            assertThat(request.name).isEqualTo(name)
            assertThat(request.birthDate).isEqualTo(birthDate)
            assertThat(request.books).isEqualTo(books)
            assertThat(request.books).hasSize(1)
        }

        @Test
        @DisplayName("本情報が空リストでオブジェクトが作成される")
        fun createAuthorRequest_emptyBooks() {
            // Given
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val books = emptyList<CreateBookWithAuthorRequest>()

            // When
            val request = CreateAuthorRequest(name, birthDate, books)

            // Then
            assertThat(request.name).isEqualTo(name)
            assertThat(request.birthDate).isEqualTo(birthDate)
            assertThat(request.books).isEmpty()
        }

        @Test
        @DisplayName("複数の本情報でオブジェクトが作成される")
        fun createAuthorRequest_multipleBooks() {
            // Given
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val books = listOf(
                CreateBookWithAuthorRequest(
                    title = BookTitle("テスト本1"),
                    bookPrice = BookPrice(1000),
                    publicationStatus = PublicationStatus.PUBLISHED
                ),
                CreateBookWithAuthorRequest(
                    title = BookTitle("テスト本2"),
                    bookPrice = BookPrice(2000),
                    publicationStatus = PublicationStatus.UNPUBLISHED
                )
            )

            // When
            val request = CreateAuthorRequest(name, birthDate, books)

            // Then
            assertThat(request.name).isEqualTo(name)
            assertThat(request.birthDate).isEqualTo(birthDate)
            assertThat(request.books).hasSize(2)
            assertThat(request.books[0].title.value).isEqualTo("テスト本1")
            assertThat(request.books[1].title.value).isEqualTo("テスト本2")
        }
    }

    @Nested
    @DisplayName("異常系テスト")
    inner class AbnormalTest {

        @Test
        @DisplayName("無効な著者名の場合、AuthorNameのバリデーションエラーが発生する")
        fun createAuthorRequest_invalidName() {
            // Given
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val books = emptyList<CreateBookWithAuthorRequest>()

            // When & Then
            assertThatThrownBy { 
                CreateAuthorRequest(AuthorName(""), birthDate, books) 
            }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("著者名は空白にできません。")
        }

        @Test
        @DisplayName("無効な生年月日の場合、BirthDateのバリデーションエラーが発生する")
        fun createAuthorRequest_invalidBirthDate() {
            // Given
            val name = AuthorName("テスト著者")
            val books = emptyList<CreateBookWithAuthorRequest>()

            // When & Then
            assertThatThrownBy { 
                CreateAuthorRequest(name, BirthDate(LocalDate.now().plusDays(1)), books) 
            }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("生年月日は現在より過去の日付でなければなりません。")
        }

        @Test
        @DisplayName("著者名が21文字以上の場合、AuthorNameのバリデーションエラーが発生する")
        fun createAuthorRequest_nameTooLong() {
            // Given
            val name = "あいうえおかきくけこさしすせそたちつてとな"
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val books = emptyList<CreateBookWithAuthorRequest>()

            // When & Then
            assertThatThrownBy { 
                CreateAuthorRequest(AuthorName(name), birthDate, books) 
            }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("著者名は20文字以内にしてください")
        }
    }

    @Nested
    @DisplayName("等価性テスト")
    inner class EqualityTest {

        @Test
        @DisplayName("同じ値のCreateAuthorRequestは等価である")
        fun createAuthorRequest_equality_sameValue() {
            // Given
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val books = listOf(
                CreateBookWithAuthorRequest(
                    title = BookTitle("テスト本"),
                    bookPrice = BookPrice(1000),
                    publicationStatus = PublicationStatus.PUBLISHED
                )
            )

            val request1 = CreateAuthorRequest(name, birthDate, books)
            val request2 = CreateAuthorRequest(name, birthDate, books)

            // When & Then
            assertThat(request1).isEqualTo(request2)
            assertThat(request1.hashCode()).isEqualTo(request2.hashCode())
        }

        @Test
        @DisplayName("異なる値のCreateAuthorRequestは等価でない")
        fun createAuthorRequest_equality_differentValue() {
            // Given
            val name1 = AuthorName("テスト著者1")
            val name2 = AuthorName("テスト著者2")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val books = emptyList<CreateBookWithAuthorRequest>()

            val request1 = CreateAuthorRequest(name1, birthDate, books)
            val request2 = CreateAuthorRequest(name2, birthDate, books)

            // When & Then
            assertThat(request1).isNotEqualTo(request2)
        }
    }

    @Nested
    @DisplayName("コピーテスト")
    inner class CopyTest {

        @Test
        @DisplayName("copy()で新しいオブジェクトが作成される")
        fun createAuthorRequest_copy() {
            // Given
            val originalName = AuthorName("テスト著者")
            val originalBirthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val originalBooks = emptyList<CreateBookWithAuthorRequest>()
            val originalRequest = CreateAuthorRequest(originalName, originalBirthDate, originalBooks)

            val newName = AuthorName("新しい著者")

            // When
            val copiedRequest = originalRequest.copy(name = newName)

            // Then
            assertThat(copiedRequest.name).isEqualTo(newName)
            assertThat(copiedRequest.birthDate).isEqualTo(originalBirthDate)
            assertThat(copiedRequest.books).isEqualTo(originalBooks)
            assertThat(copiedRequest).isNotEqualTo(originalRequest)
        }
    }
}
