package com.example.book_management.dto.author

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

@DisplayName("BirthDate 単体テスト")
class BirthDateTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("過去の日付（昨日）でオブジェクトが作成される")
        fun createBirthDate_yesterday() {
            // Given
            val yesterday = LocalDate.now().minusDays(1)

            // When
            val birthDate = BirthDate(yesterday)

            // Then
            assertThat(birthDate.value).isEqualTo(yesterday)
        }

        @Test
        @DisplayName("過去の日付（1年前）でオブジェクトが作成される")
        fun createBirthDate_oneYearAgo() {
            // Given
            val oneYearAgo = LocalDate.now().minusYears(1)

            // When
            val birthDate = BirthDate(oneYearAgo)

            // Then
            assertThat(birthDate.value).isEqualTo(oneYearAgo)
        }
    }

    @Nested
    @DisplayName("異常系テスト")
    inner class AbnormalTest {

        @Test
        @DisplayName("今日の日付の場合、IllegalArgumentExceptionが発生する")
        fun createBirthDate_today() {
            // Given
            val today = LocalDate.now()

            // When & Then
            assertThatThrownBy { BirthDate(today) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("生年月日は現在より過去の日付でなければなりません。")
        }

        @Test
        @DisplayName("未来の日付（明日）の場合、IllegalArgumentExceptionが発生する")
        fun createBirthDate_tomorrow() {
            // Given
            val tomorrow = LocalDate.now().plusDays(1)

            // When & Then
            assertThatThrownBy { BirthDate(tomorrow) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("生年月日は現在より過去の日付でなければなりません。")
        }
    }

    @Nested
    @DisplayName("等価性テスト")
    inner class EqualityTest {

        @Test
        @DisplayName("同じ値のBirthDateは等価である")
        fun birthDate_equality_sameValue() {
            // Given
            val date = LocalDate.of(1990, 1, 1)
            val birthDate1 = BirthDate(date)
            val birthDate2 = BirthDate(date)

            // When & Then
            assertThat(birthDate1).isEqualTo(birthDate2)
            assertThat(birthDate1.hashCode()).isEqualTo(birthDate2.hashCode())
        }

        @Test
        @DisplayName("異なる値のBirthDateは等価でない")
        fun birthDate_equality_differentValue() {
            // Given
            val birthDate1 = BirthDate(LocalDate.of(1990, 1, 1))
            val birthDate2 = BirthDate(LocalDate.of(1991, 1, 1))

            // When & Then
            assertThat(birthDate1).isNotEqualTo(birthDate2)
        }
    }
}

