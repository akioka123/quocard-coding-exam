package com.example.book_management.dto.author

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("AuthorName 単体テスト")
class AuthorNameTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("有効な著者名（1文字）でオブジェクトが作成される")
        fun createAuthorName_validOneCharacter() {
            // Given
            val name = "あ"

            // When
            val authorName = AuthorName(name)

            // Then
            assertThat(authorName.value).isEqualTo(name)
        }

        @Test
        @DisplayName("有効な著者名（20文字）でオブジェクトが作成される")
        fun createAuthorName_validTwentyCharacters() {
            // Given
            val name = "あいうえおかきくけこさしすせそたちつてと"

            // When
            val authorName = AuthorName(name)

            // Then
            assertThat(authorName.value).isEqualTo(name)
        }

        @Test
        @DisplayName("有効な著者名（10文字）でオブジェクトが作成される")
        fun createAuthorName_validTenCharacters() {
            // Given
            val name = "あいうえおかきくけこ"

            // When
            val authorName = AuthorName(name)

            // Then
            assertThat(authorName.value).isEqualTo(name)
        }

        @Test
        @DisplayName("英数字の著者名でオブジェクトが作成される")
        fun createAuthorName_validAlphanumeric() {
            // Given
            val name = "Author123"

            // When
            val authorName = AuthorName(name)

            // Then
            assertThat(authorName.value).isEqualTo(name)
        }

        @Test
        @DisplayName("記号を含む著者名でオブジェクトが作成される")
        fun createAuthorName_validWithSymbols() {
            // Given
            val name = "Author-Name"

            // When
            val authorName = AuthorName(name)

            // Then
            assertThat(authorName.value).isEqualTo(name)
        }
    }

    @Nested
    @DisplayName("異常系テスト")
    inner class AbnormalTest {

        @Test
        @DisplayName("空文字の場合、IllegalArgumentExceptionが発生する")
        fun createAuthorName_emptyString() {
            // Given
            val name = ""

            // When & Then
            assertThatThrownBy { AuthorName(name) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("著者名は空白にできません。")
        }

        @Test
        @DisplayName("空白のみの場合、IllegalArgumentExceptionが発生する")
        fun createAuthorName_blankOnly() {
            // Given
            val name = "   "

            // When & Then
            assertThatThrownBy { AuthorName(name) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("著者名は空白にできません。")
        }

        @Test
        @DisplayName("タブのみの場合、IllegalArgumentExceptionが発生する")
        fun createAuthorName_tabOnly() {
            // Given
            val name = "\t"

            // When & Then
            assertThatThrownBy { AuthorName(name) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("著者名は空白にできません。")
        }

        @Test
        @DisplayName("改行のみの場合、IllegalArgumentExceptionが発生する")
        fun createAuthorName_newlineOnly() {
            // Given
            val name = "\n"

            // When & Then
            assertThatThrownBy { AuthorName(name) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("著者名は空白にできません。")
        }

        @Test
        @DisplayName("21文字以上の場合、IllegalArgumentExceptionが発生する")
        fun createAuthorName_overTwentyCharacters() {
            // Given
            val name = "あいうえおかきくけこさしすせそたちつてとな"

            // When & Then
            assertThatThrownBy { AuthorName(name) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("著者名は20文字以内にしてください")
        }

        @Test
        @DisplayName("50文字の場合、IllegalArgumentExceptionが発生する")
        fun createAuthorName_fiftyCharacters() {
            // Given
            val name = "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをん"

            // When & Then
            assertThatThrownBy { AuthorName(name) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("著者名は20文字以内にしてください")
        }
    }

    @Nested
    @DisplayName("等価性テスト")
    inner class EqualityTest {

        @Test
        @DisplayName("同じ値のAuthorNameは等価である")
        fun authorName_equality_sameValue() {
            // Given
            val name = "テスト著者"
            val authorName1 = AuthorName(name)
            val authorName2 = AuthorName(name)

            // When & Then
            assertThat(authorName1).isEqualTo(authorName2)
            assertThat(authorName1.hashCode()).isEqualTo(authorName2.hashCode())
        }

        @Test
        @DisplayName("異なる値のAuthorNameは等価でない")
        fun authorName_equality_differentValue() {
            // Given
            val authorName1 = AuthorName("テスト著者1")
            val authorName2 = AuthorName("テスト著者2")

            // When & Then
            assertThat(authorName1).isNotEqualTo(authorName2)
        }
    }
}

