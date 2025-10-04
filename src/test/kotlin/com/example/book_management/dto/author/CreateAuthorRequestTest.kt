package com.example.book_management.dto.author

import com.example.book_management.dto.book.BookId
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*

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
            val bookIds = listOf(BookId(UUID.randomUUID()))

            // When
            val request = CreateAuthorRequest(name, birthDate, bookIds)

            // Then
            assertThat(request.name).isEqualTo(name)
            assertThat(request.birthDate).isEqualTo(birthDate)
            assertThat(request.bookIds).isEqualTo(bookIds)
            assertThat(request.bookIds).hasSize(1)
        }

        @Test
        @DisplayName("本情報が空リストでオブジェクトが作成される")
        fun createAuthorRequest_emptyBooks() {
            // Given
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val bookIds = emptyList<BookId>()

            // When
            val request = CreateAuthorRequest(name, birthDate, bookIds)

            // Then
            assertThat(request.name).isEqualTo(name)
            assertThat(request.birthDate).isEqualTo(birthDate)
            assertThat(request.bookIds).isEmpty()
        }

        @Test
        @DisplayName("複数の本情報でオブジェクトが作成される")
        fun createAuthorRequest_multipleBooks() {
            // Given
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val bookIds = listOf(
                BookId(UUID.fromString("11111111-1111-1111-1111-111111111111")),
                BookId(UUID.fromString("22222222-2222-2222-2222-222222222222"))
            )

            // When
            val request = CreateAuthorRequest(name, birthDate, bookIds)

            // Then
            assertThat(request.name).isEqualTo(name)
            assertThat(request.birthDate).isEqualTo(birthDate)
            assertThat(request.bookIds).hasSize(2)
            assertThat(request.bookIds[0].value).isEqualTo(UUID.fromString("11111111-1111-1111-1111-111111111111"))
            assertThat(request.bookIds[1].value).isEqualTo(UUID.fromString("22222222-2222-2222-2222-222222222222"))
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
            val bookIds = emptyList<BookId>()

            // When & Then
            assertThatThrownBy {
                CreateAuthorRequest(AuthorName(""), birthDate, bookIds)
            }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("著者名は空白にできません。")
        }

        @Test
        @DisplayName("無効な生年月日の場合、BirthDateのバリデーションエラーが発生する")
        fun createAuthorRequest_invalidBirthDate() {
            // Given
            val name = AuthorName("テスト著者")
            val bookIds = emptyList<BookId>()

            // When & Then
            assertThatThrownBy {
                CreateAuthorRequest(name, BirthDate(LocalDate.now().plusDays(1)), bookIds)
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
            val bookIds = emptyList<BookId>()

            // When & Then
            assertThatThrownBy {
                CreateAuthorRequest(AuthorName(name), birthDate, bookIds)
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
            val bookIds = listOf(
                BookId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
            )

            val request1 = CreateAuthorRequest(name, birthDate, bookIds)
            val request2 = CreateAuthorRequest(name, birthDate, bookIds)

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
            val bookIds = emptyList<BookId>()

            val request1 = CreateAuthorRequest(name1, birthDate, bookIds)
            val request2 = CreateAuthorRequest(name2, birthDate, bookIds)

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
            val bookIds = emptyList<BookId>()
            val originalRequest = CreateAuthorRequest(originalName, originalBirthDate, bookIds)

            val newName = AuthorName("新しい著者")

            // When
            val copiedRequest = originalRequest.copy(name = newName)

            // Then
            assertThat(copiedRequest.name).isEqualTo(newName)
            assertThat(copiedRequest.birthDate).isEqualTo(originalBirthDate)
            assertThat(copiedRequest.bookIds).isEqualTo(bookIds)
            assertThat(copiedRequest).isNotEqualTo(originalRequest)
        }
    }
}
