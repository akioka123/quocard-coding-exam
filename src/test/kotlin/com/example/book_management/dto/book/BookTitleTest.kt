package com.example.book_management.dto.book

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("BookTitle 単体テスト")
class BookTitleTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("正常な文字列でBookTitleを作成できる")
        fun constructor_validString() {
            // Given
            val title = "テストタイトル"

            // When
            val bookTitle = BookTitle(title)

            // Then
            assertThat(bookTitle.value).isEqualTo(title)
        }

        @Test
        @DisplayName("1文字の最小文字列でBookTitleを作成できる")
        fun constructor_singleCharacter() {
            // Given
            val title = "A"

            // When
            val bookTitle = BookTitle(title)

            // Then
            assertThat(bookTitle.value).isEqualTo(title)
        }

        @Test
        @DisplayName("100文字の最大文字列でBookTitleを作成できる")
        fun constructor_maxLengthString() {
            // Given
            val title = "A".repeat(100) // 100文字

            // When
            val bookTitle = BookTitle(title)

            // Then    
            assertThat(bookTitle.value).isEqualTo(title)
            assertThat(bookTitle.value.length).isEqualTo(100)
        }
    }

    @Nested
    @DisplayName("異常系テスト")
    inner class AbnormalTest {

        @Test
        @DisplayName("空文字でBookTitle作成時に例外発生")
        fun constructor_emptyString() {
            // Given
            val title = ""

            // When & Then
            assertThatThrownBy { BookTitle(title) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("書籍タイトルは空白にできません。")
        }

        @Test
        @DisplayName("空白のみでBookTitle作成時に例外発生")
        fun constructor_whitespaceOnly() {
            // Given
            val title = "   "

            // When & Then
            assertThatThrownBy { BookTitle(title) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("書籍タイトルは空白にできません。")
        }

        @Test
        @DisplayName("タブのみでBookTitle作成時に例外発生")
        fun constructor_tabOnly() {
            // Given
            val title = "\t"

            // When & Then
            assertThatThrownBy { BookTitle(title) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("書籍タイトルは空白にできません。")
        }

        @Test
        @DisplayName("改行のみでBookTitle作成時に例外発生")
        fun constructor_newlineOnly() {
            // Given
            val title = "\n"

            // When & Then
            assertThatThrownBy { BookTitle(title) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("書籍タイトルは空白にできません。")
        }

        @Test
        @DisplayName("101文字でBookTitle作成時に例外発生")
        fun constructor_exceedMaxLength() {
            // Given
            val title = "A".repeat(101) // 101文字

            // When & Then
            assertThatThrownBy { BookTitle(title) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("書籍タイトルは100文字以内でなければなりません。")
        }

        @Test
        @DisplayName("非常に長い文字列でBookTitle作成時に例外発生")
        fun constructor_veryLongString() {
            // Given
            val title = "A".repeat(1000) // 1000文字

            // When & Then
            assertThatThrownBy { BookTitle(title) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("書籍タイトルは100文字以内でなければなりません。")
        }
    }

    @Nested
    @DisplayName("等価性・比較テスト")
    inner class EqualityTest {

        @Test
        @DisplayName("同じ文字列を持つBookTitleは等価")
        fun equals_sameValue() {
            // Given
            val title = "テストタイトル"
            val bookTitle1 = BookTitle(title)
            val bookTitle2 = BookTitle(title)

            // Then
            assertThat(bookTitle1).isEqualTo(bookTitle2)
        }

        @Test
        @DisplayName("異なる文字列を持つBookTitleは不等価")
        fun equals_differentValue() {
            // Given
            val bookTitle1 = BookTitle("タイトル1")
            val bookTitle2 = BookTitle("タイトル2")

            // Then
            assertThat(bookTitle1).isNotEqualTo(bookTitle2)
        }

        @Test
        @DisplayName("同じ文字列を持つBookTitleは同じハッシュコード")
        fun hashCode_sameValue() {
            // Given
            val title = "テストタイトル"
            val bookTitle1 = BookTitle(title)
            val bookTitle2 = BookTitle(title)

            // Then
            assertThat(bookTitle1.hashCode()).isEqualTo(bookTitle2.hashCode())
        }
    }
}
