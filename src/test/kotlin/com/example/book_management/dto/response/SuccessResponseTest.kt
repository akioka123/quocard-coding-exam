package com.example.book_management.dto.response

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

@DisplayName("SuccessResponse 単体テスト")
class SuccessResponseTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("正常なHttpStatusとデータでSuccessResponseを作成できる")
        fun constructor_validStatusCodeAndData() {
            // Given
            val statusCode = HttpStatus.CREATED
            val data = "Test data"

            // When
            val response = SuccessResponse(statusCode = statusCode, data = data)

            // Then
            assertThat(response.statusCode).isEqualTo(statusCode)
            assertThat(response.data).isEqualTo(data)
        }

        @Test
        @DisplayName("異なるHttpStatusでSuccessResponseを作成できる")
        fun constructor_differentHttpStatus() {
            // Given
            val statusCode = HttpStatus.OK
            val data = mapOf("key" to "value")

            // When
            val response = SuccessResponse(statusCode = statusCode, data = data)

            // Then
            assertThat(response.statusCode).isEqualTo(statusCode)
            assertThat(response.data).isEqualTo(data)
        }

        @Test
        @DisplayName("ジェネリック型StringのデータでSuccessResponseを作成できる")
        fun constructor_genericTypeString() {
            // Given
            val statusCode = HttpStatus.OK
            val data = "Success message"

            // When
            val response: SuccessResponse<String> = SuccessResponse(statusCode = statusCode, data = data)

            // Then
            assertThat(response.statusCode).isEqualTo(statusCode)
            assertThat(response.data).isEqualTo(data)
        }

        @Test
        @DisplayName("ジェネリック型ListのデータでSuccessResponseを作成できる")
        fun constructor_genericTypeList() {
            // Given
            val statusCode = HttpStatus.OK
            val data = listOf("item1", "item2", "item3")

            // When
            val response: SuccessResponse<List<String>> = SuccessResponse(statusCode = statusCode, data = data)

            // Then
            assertThat(response.statusCode).isEqualTo(statusCode)
            assertThat(response.data).isEqualTo(data)
        }
    }

    @Nested
    @DisplayName("Data Class固有テスト")
    inner class DataClassTest {

        @Test
        @DisplayName("同じ値を持つSuccessResponseは等価")
        fun equals_sameValues() {
            // Given
            val statusCode = HttpStatus.CREATED
            val data = "Test data"
            val response1 = SuccessResponse(statusCode = statusCode, data = data)
            val response2 = SuccessResponse(statusCode = statusCode, data = data)

            // Then
            assertThat(response1).isEqualTo(response2)
        }

        @Test
        @DisplayName("異なる値を持つSuccessResponseは不等価")
        fun equals_differentValues() {
            // Given
            val response1 = SuccessResponse(statusCode = HttpStatus.CREATED, data = "data1")
            val response2 = SuccessResponse(statusCode = HttpStatus.OK, data = "data2")

            // Then
            assertThat(response1).isNotEqualTo(response2)
        }

        @Test
        @DisplayName("同じ値を持つSuccessResponseは同じハッシュコード")
        fun hashCode_sameValues() {
            // Given
            val statusCode = HttpStatus.CREATED
            val data = "Test data"
            val response1 = SuccessResponse(statusCode = statusCode, data = data)
            val response2 = SuccessResponse(statusCode = statusCode, data = data)

            // Then
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode())
        }

        @Test
        @DisplayName("copyで別の値でコピー作成できる")
        fun copy_differentValues() {
            // Given
            val originalResponse = SuccessResponse(statusCode = HttpStatus.CREATED, data = "original data")

            // When
            val copiedResponse = originalResponse.copy(data = "copied data")

            // Then
            assertThat(copiedResponse.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(copiedResponse.data).isEqualTo("copied data")
        }
    }
}
