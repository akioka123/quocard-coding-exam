package com.example.book_management.dto.author

import com.example.book_management.tables.records.AuthorsRecord
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@DisplayName("Author 単体テスト")
class AuthorTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("有効な著者情報でオブジェクトが作成される")
        fun createAuthor_validData() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val createdAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            val updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)

            // When
            val author = Author(id, name, birthDate, createdAt, updatedAt)

            // Then
            assertThat(author.id).isEqualTo(id)
            assertThat(author.name).isEqualTo(name)
            assertThat(author.birthDate).isEqualTo(birthDate)
            assertThat(author.createdAt).isEqualTo(createdAt)
            assertThat(author.updatedAt).isEqualTo(updatedAt)
        }

        @Test
        @DisplayName("fromRecordで有効なレコードからオブジェクトが作成される")
        fun fromRecord_validRecord() {
            // Given
            val id = UUID.randomUUID()
            val name = "テスト著者"
            val birthDate = LocalDate.of(1990, 1, 1)
            val createdAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            val updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)

            val record = AuthorsRecord()
            record.id = id
            record.name = name
            record.birthDate = birthDate
            record.createdAt = createdAt
            record.updatedAt = updatedAt

            // When
            val author = Author.fromRecord(record)

            // Then
            assertThat(author.id.value).isEqualTo(id)
            assertThat(author.name.value).isEqualTo(name)
            assertThat(author.birthDate.value).isEqualTo(birthDate)
            assertThat(author.createdAt).isEqualTo(createdAt)
            assertThat(author.updatedAt).isEqualTo(updatedAt)
        }

        @Test
        @DisplayName("fromRecordで最小文字数の著者名からオブジェクトが作成される")
        fun fromRecord_minNameLength() {
            // Given
            val id = UUID.randomUUID()
            val name = "あ"
            val birthDate = LocalDate.of(1990, 1, 1)
            val createdAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            val updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)

            val record = AuthorsRecord()
            record.id = id
            record.name = name
            record.birthDate = birthDate
            record.createdAt = createdAt
            record.updatedAt = updatedAt

            // When
            val author = Author.fromRecord(record)

            // Then
            assertThat(author.id.value).isEqualTo(id)
            assertThat(author.name.value).isEqualTo(name)
            assertThat(author.birthDate.value).isEqualTo(birthDate)
            assertThat(author.createdAt).isEqualTo(createdAt)
            assertThat(author.updatedAt).isEqualTo(updatedAt)
        }

        @Test
        @DisplayName("fromRecordで最大文字数の著者名からオブジェクトが作成される")
        fun fromRecord_maxNameLength() {
            // Given
            val id = UUID.randomUUID()
            val name = "あいうえおかきくけこさしすせそたちつてと"
            val birthDate = LocalDate.of(1990, 1, 1)
            val createdAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            val updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)

            val record = AuthorsRecord()
            record.id = id
            record.name = name
            record.birthDate = birthDate
            record.createdAt = createdAt
            record.updatedAt = updatedAt

            // When
            val author = Author.fromRecord(record)

            // Then
            assertThat(author.id.value).isEqualTo(id)
            assertThat(author.name.value).isEqualTo(name)
            assertThat(author.birthDate.value).isEqualTo(birthDate)
            assertThat(author.createdAt).isEqualTo(createdAt)
            assertThat(author.updatedAt).isEqualTo(updatedAt)
        }

        @Test
        @DisplayName("fromRecordで過去の生年月日からオブジェクトが作成される")
        fun fromRecord_pastBirthDate() {
            // Given
            val id = UUID.randomUUID()
            val name = "テスト著者"
            val birthDate = LocalDate.now().minusYears(30)
            val createdAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            val updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)

            val record = AuthorsRecord()
            record.id = id
            record.name = name
            record.birthDate = birthDate
            record.createdAt = createdAt
            record.updatedAt = updatedAt

            // When
            val author = Author.fromRecord(record)

            // Then
            assertThat(author.id.value).isEqualTo(id)
            assertThat(author.name.value).isEqualTo(name)
            assertThat(author.birthDate.value).isEqualTo(birthDate)
            assertThat(author.createdAt).isEqualTo(createdAt)
            assertThat(author.updatedAt).isEqualTo(updatedAt)
        }
    }

    @Nested
    @DisplayName("異常系テスト")
    inner class AbnormalTest {

        @Test
        @DisplayName("fromRecordでIDがnullの場合、IllegalStateExceptionが発生する")
        fun fromRecord_nullId() {
            // Given
            val record = AuthorsRecord()
            record.id = null
            record.name = "テスト著者"
            record.birthDate = LocalDate.of(1990, 1, 1)
            record.createdAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            record.updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)

            // When & Then
            assertThatThrownBy { Author.fromRecord(record) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("著者IDがnullです")
        }

        @Test
        @DisplayName("fromRecordで名前がnullの場合、IllegalStateExceptionが発生する")
        fun fromRecord_nullName() {
            // Given
            val record = AuthorsRecord()
            record.id = UUID.randomUUID()
            record.name = null
            record.birthDate = LocalDate.of(1990, 1, 1)
            record.createdAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            record.updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)

            // When & Then
            assertThatThrownBy { Author.fromRecord(record) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("著者名がnullです")
        }

        @Test
        @DisplayName("fromRecordで生年月日がnullの場合、IllegalStateExceptionが発生する")
        fun fromRecord_nullBirthDate() {
            // Given
            val record = AuthorsRecord()
            record.id = UUID.randomUUID()
            record.name = "テスト著者"
            record.birthDate = null
            record.createdAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            record.updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)

            // When & Then
            assertThatThrownBy { Author.fromRecord(record) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("生年月日がnullです")
        }

        @Test
        @DisplayName("fromRecordで作成日時がnullの場合、IllegalStateExceptionが発生する")
        fun fromRecord_nullCreatedAt() {
            // Given
            val record = AuthorsRecord()
            record.id = UUID.randomUUID()
            record.name = "テスト著者"
            record.birthDate = LocalDate.of(1990, 1, 1)
            record.createdAt = null
            record.updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)

            // When & Then
            assertThatThrownBy { Author.fromRecord(record) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("作成日時がnullです")
        }

        @Test
        @DisplayName("fromRecordで更新日時がnullの場合、IllegalStateExceptionが発生する")
        fun fromRecord_nullUpdatedAt() {
            // Given
            val record = AuthorsRecord()
            record.id = UUID.randomUUID()
            record.name = "テスト著者"
            record.birthDate = LocalDate.of(1990, 1, 1)
            record.createdAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            record.updatedAt = null

            // When & Then
            assertThatThrownBy { Author.fromRecord(record) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("更新日時がnullです")
        }

        @Test
        @DisplayName("fromRecordで複数のフィールドがnullの場合、最初のnullフィールドでエラーが発生する")
        fun fromRecord_multipleNullFields() {
            // Given
            val record = AuthorsRecord()
            record.id = null
            record.name = null
            record.birthDate = null
            record.createdAt = null
            record.updatedAt = null

            // When & Then
            assertThatThrownBy { Author.fromRecord(record) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("著者IDがnullです")
        }

        @Test
        @DisplayName("fromRecordで無効な著者名の場合、AuthorNameのバリデーションエラーが発生する")
        fun fromRecord_invalidName() {
            // Given
            val record = AuthorsRecord()
            record.id = UUID.randomUUID()
            record.name = "" // 空文字
            record.birthDate = LocalDate.of(1990, 1, 1)
            record.createdAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            record.updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)

            // When & Then
            assertThatThrownBy { Author.fromRecord(record) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("著者名は空白にできません。")
        }

        @Test
        @DisplayName("fromRecordで無効な生年月日の場合、BirthDateのバリデーションエラーが発生する")
        fun fromRecord_invalidBirthDate() {
            // Given
            val record = AuthorsRecord()
            record.id = UUID.randomUUID()
            record.name = "テスト著者"
            record.birthDate = LocalDate.now().plusDays(1) // 未来の日付
            record.createdAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            record.updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)

            // When & Then
            assertThatThrownBy { Author.fromRecord(record) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("生年月日は現在より過去の日付でなければなりません。")
        }
    }

    @Nested
    @DisplayName("等価性テスト")
    inner class EqualityTest {

        @Test
        @DisplayName("同じ値のAuthorは等価である")
        fun author_equality_sameValue() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val createdAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            val updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)

            val author1 = Author(id, name, birthDate, createdAt, updatedAt)
            val author2 = Author(id, name, birthDate, createdAt, updatedAt)

            // When & Then
            assertThat(author1).isEqualTo(author2)
            assertThat(author1.hashCode()).isEqualTo(author2.hashCode())
        }

        @Test
        @DisplayName("異なる値のAuthorは等価でない")
        fun author_equality_differentValue() {
            // Given
            val name = AuthorName("テスト著者")
            val birthDate = BirthDate(LocalDate.of(1990, 1, 1))
            val createdAt = LocalDateTime.of(2024, 1, 1, 0, 0)
            val updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0)

            val author1 = Author(AuthorId(UUID.randomUUID()), name, birthDate, createdAt, updatedAt)
            val author2 = Author(AuthorId(UUID.randomUUID()), name, birthDate, createdAt, updatedAt)

            // When & Then
            assertThat(author1).isNotEqualTo(author2)
        }
    }
}

