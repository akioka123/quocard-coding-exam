package com.example.book_management.dto.book

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@DisplayName("BookPrice 単体テスト")
class BookPriceTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("BigDecimalで正常な価格を作成できる")
        fun constructor_bigDecimal() {
            // Given
            val price = BigDecimal("1500.50")

            // When
            val bookPrice = BookPrice(price)

            // Then
            assertThat(bookPrice.value).isEqualTo(price)
        }

        @Test
        @DisplayName("Intで正常な価格を作成できる")
        fun constructor_int() {
            // Given
            val price = 1000

            // When
            val bookPrice = BookPrice(price)

            // Then
            assertThat(bookPrice.value).isEqualTo(BigDecimal(price))
        }

        @Test
        @DisplayName("Longで正常な価格を作成できる")
        fun constructor_long() {
            // Given
            val price = 2000L

            // When
            val bookPrice = BookPrice(price)

            // Then
            assertThat(bookPrice.value).isEqualTo(BigDecimal(price))
        }

        @Test
        @DisplayName("Doubleで正常な価格を作成できる")
        fun constructor_double() {
            // Given
            val price = 1234.56

            // When
            val bookPrice = BookPrice(price)

            // Then
            assertThat(bookPrice.value).isEqualTo(BigDecimal.valueOf(price))
        }

        @Test
        @DisplayName("境界値0でBookPriceを作成できる")
        fun constructor_zero() {
            // Given
            val price = BigDecimal.ZERO

            // When
            val bookPrice = BookPrice(price)

            // Then
            assertThat(bookPrice.value).isEqualTo(BigDecimal.ZERO)
        }

        @Test
        @DisplayName("価格上限の境界値でBookPriceを作成できる")
        fun constructor_maxPrice() {
            // Given
            val price = BigDecimal("999999999999.99") // 12桁整数部

            // When
            val bookPrice = BookPrice(price)

            // Then
            assertThat(bookPrice.value).isEqualTo(price)
        }
    }

    @Nested
    @DisplayName("異常系テスト")
    inner class AbnormalTest {

        @Test
        @DisplayName("負のBigDecimalでBookPrice作成時に例外発生")
        fun constructor_negativeBigDecimal() {
            // Given
            val price = BigDecimal("-100.50")

            // When & Then
            assertThatThrownBy { BookPrice(price) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("価格は0以上でなければなりません。")
        }

        @Test
        @DisplayName("負のIntでBookPrice作成時に例外発生")
        fun constructor_negativeInt() {
            // Given
            val price = -1000

            // When & Then
            assertThatThrownBy { BookPrice(price) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("価格は0以上でなければなりません。")
        }

        @Test
        @DisplayName("負のLongでBookPrice作成時に例外発生")
        fun constructor_negativeLong() {
            // Given
            val price = -2000L

            // When & Then
            assertThatThrownBy { BookPrice(price) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("価格は0以上でなければなりません。")
        }

        @Test
        @DisplayName("負のDoubleでBookPrice作成時に例外発生")
        fun constructor_negativeDouble() {
            // Given
            val price = -1234.56

            // When & Then
            assertThatThrownBy { BookPrice(price) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("価格は0以上でなければなりません。")
        }

        @Test
        @DisplayName("価格上限を超えるBigDecimalでBookPrice作成時に例外発生")
        fun constructor_exceedMaxPriceBigDecimal() {
            // Given
            val price = BigDecimal("1000000000000.00") // 13桁整数部

            // When & Then
            assertThatThrownBy { BookPrice(price) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("価格は整数部12桁以内でなければなりません。")
        }

        @Test
        @DisplayName("価格上限を超えるLongでBookPrice作成時に例外発生")
        fun constructor_exceedMaxPriceLong() {
            // Given
            val price = 1000000000000L // 13桁整数部

            // When & Then
            assertThatThrownBy { BookPrice(price) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("価格は整数部12桁以内でなければなりません。")
        }
    }

    @Nested
    @DisplayName("等価性・比較テスト")
    inner class EqualityTest {

        @Test
        @DisplayName("同じ値を持つBookPriceは等価")
        fun equals_sameValue() {
            // Given
            val price = BigDecimal("1500.50")
            val bookPrice1 = BookPrice(price)
            val bookPrice2 = BookPrice(price)

            // Then
            assertThat(bookPrice1).isEqualTo(bookPrice2)
        }

        @Test
        @DisplayName("異なる値を持つBookPriceは不等価")
        fun equals_differentValue() {
            // Given
            val bookPrice1 = BookPrice(BigDecimal("1000"))
            val bookPrice2 = BookPrice(BigDecimal("2000"))

            // Then
            assertThat(bookPrice1).isNotEqualTo(bookPrice2)
        }

        @Test
        @DisplayName("同じ値を持つBookPriceは同じハッシュコード")
        fun hashCode_sameValue() {
            // Given
            val price = BigDecimal("1500.50")
            val bookPrice1 = BookPrice(price)
            val bookPrice2 = BookPrice(price)

            // Then
            assertThat(bookPrice1.hashCode()).isEqualTo(bookPrice2.hashCode())
        }
    }
}
