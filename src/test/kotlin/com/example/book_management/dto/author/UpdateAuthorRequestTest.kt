package com.example.book_management.dto.author

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*

@DisplayName("UpdateAuthorRequest 単体テスト")
class UpdateAuthorRequestTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("正常な値オブジェクトでUpdateAuthorRequestを作成できる")
        fun constructor_validValueObjects() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("田中 太郎")
            val birthDate = BirthDate(LocalDate.of(1990, 5, 15))

            // When
            val request = UpdateAuthorRequest(id = id, name = name, birthDate = birthDate)

            // Then
            assertThat(request.id).isEqualTo(id)
            assertThat(request.name).isEqualTo(name)
            assertThat(request.birthDate).isEqualTo(birthDate)
        }

        @Test
        @DisplayName("長い著者名でUpdateAuthorRequestを作成できる")
        fun constructor_longAuthorName() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("田中太郎佐藤花子田村田中太郎佐藤花子田村") // 20文字境界値
            val birthDate = BirthDate(LocalDate.of(1985, 12, 31))

            // When
            val request = UpdateAuthorRequest(id = id, name = name, birthDate = birthDate)

            // Then
            assertThat(request.name).isEqualTo(name)
            assertThat(request.name.value).hasSize(20)
        }

        @Test
        @DisplayName("過去の古い生年月日でUpdateAuthorRequestを作成できる")
        fun constructor_oldBirthDate() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("山田 花子")
            val birthDate = BirthDate(LocalDate.of(1950, 1, 1))

            // When
            val request = UpdateAuthorRequest(id = id, name = name, birthDate = birthDate)

            // Then
            assertThat(request.birthDate).isEqualTo(birthDate)
            assertThat(request.birthDate.value).isBefore(LocalDate.now())
        }
    }

    @Nested
    @DisplayName("Data Class固有テスト")
    inner class DataClassTest {

        @Test
        @DisplayName("同じ値オブジェクトを持つUpdateAuthorRequestは等価")
        fun equals_sameValues() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("田中 太郎")
            val birthDate = BirthDate(LocalDate.of(1990, 5, 15))

            val request1 = UpdateAuthorRequest(id = id, name = name, birthDate = birthDate)
            val request2 = UpdateAuthorRequest(id = id, name = name, birthDate = birthDate)

            // Then
            assertThat(request1).isEqualTo(request2)
        }

        @Test
        @DisplayName("異なる値オブジェクトを持つUpdateAuthorRequestは不等価")
        fun equals_differentValues() {
            // Given
            val request1 = UpdateAuthorRequest(
                id = AuthorId(UUID.randomUUID()),
                name = AuthorName("田中 太郎"),
                birthDate = BirthDate(LocalDate.of(1990, 5, 15))
            )
            val request2 = UpdateAuthorRequest(
                id = AuthorId(UUID.randomUUID()),
                name = AuthorName("佐藤 花子"),
                birthDate = BirthDate(LocalDate.of(1985, 8, 20))
            )

            // Then
            assertThat(request1).isNotEqualTo(request2)
        }

        @Test
        @DisplayName("同じ値オブジェクトを持つUpdateAuthorRequestは同じハッシュコード")
        fun hashCode_sameValues() {
            // Given
            val id = AuthorId(UUID.randomUUID())
            val name = AuthorName("田中 太郎")
            val birthDate = BirthDate(LocalDate.of(1990, 5, 15))

            val request1 = UpdateAuthorRequest(id = id, name = name, birthDate = birthDate)
            val request2 = UpdateAuthorRequest(id = id, name = name, birthDate = birthDate)

            // Then
            assertThat(request1.hashCode()).isEqualTo(request2.hashCode())
        }

        @Test
        @DisplayName("copyで別の値でコピー作成できる")
        fun copy_differentValues() {
            // Given
            val originalRequest = UpdateAuthorRequest(
                id = AuthorId(UUID.randomUUID()),
                name = AuthorName("元の名前"),
                birthDate = BirthDate(LocalDate.of(1990, 5, 15))
            )

            // When
            val copiedRequest = originalRequest.copy(
                name = AuthorName("新しい名前"),
                birthDate = BirthDate(LocalDate.of(1988, 10, 25))
            )

            // Then
            assertThat(copiedRequest.id).isEqualTo(originalRequest.id)
            assertThat(copiedRequest.name.value).isEqualTo("新しい名前")
            assertThat(copiedRequest.birthDate.value).isEqualTo(LocalDate.of(1988, 10, 25))
        }
    }
}

