package com.example.book_management.dto.response

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

@DisplayName("ErrorResponse 単体テスト")
class ErrorResponseTest {

    @Nested
    @DisplayName("正常系テスト")
    inner class NormalTest {

        @Test
        @DisplayName("正常なHttpStatusとエラーメッセージでErrorResponseを作成できる")
        fun constructor_validStatusCodeAndErrorMessage() {
            // Given
            val statusCode = HttpStatus.BAD_REQUEST
            val errorMessage = "Validation error occurred"

            // When
            val response = ErrorResponse(statusCode = statusCode, errorMessage = errorMessage)

            // Then
            assertThat(response.statusCode).isEqualTo(statusCode)
            assertThat(response.errorMessage).isEqualTo(errorMessage)
        }

        @Test
        @DisplayName("異なるHttpStatusでErrorResponseを作成できる")
        fun constructor_differentHttpStatus() {
            // Given
            val statusCode = HttpStatus.INTERNAL_SERVER_ERROR
            val errorMessage = "Internal server error"

            // When
            val response = ErrorResponse(statusCode = statusCode, errorMessage = errorMessage)

            // Then
            assertThat(response.statusCode).isEqualTo(statusCode)
            assertThat(response.errorMessage).isEqualTo(errorMessage)
        }

        @Test
        @DisplayName("空のエラーメッセージでErrorResponseを作成できる")
        fun constructor_emptyErrorMessage() {
            // Given
            val statusCode = HttpStatus.NOT_FOUND
            val errorMessage = ""

            // When
            val response = ErrorResponse(statusCode = statusCode, errorMessage = errorMessage)

            // Then
            assertThat(response.statusCode).isEqualTo(statusCode)
            assertThat(response.errorMessage).isEqualTo(errorMessage)
        }
    }

    @Nested
    @DisplayName("Data Class固有テスト")
    inner class DataClassTest {

        @Test
        @DisplayName("同じ値を持つErrorResponseは等価")
        fun equals_sameValues() {
            // Given
            val statusCode = HttpStatus.BAD_REQUEST
            val errorMessage = "Validation error occurred"
            val response1 = ErrorResponse(statusCode = statusCode, errorMessage = errorMessage)
            val response2 = ErrorResponse(statusCode = statusCode, errorMessage = errorMessage)

            // Then
            assertThat(response1).isEqualTo(response2)
        }

        @Test
        @DisplayName("異なる値を持つErrorResponseは不等価")
        fun equals_differentValues() {
            // Given
            val response1 = ErrorResponse(statusCode = HttpStatus.BAD_REQUEST, errorMessage = "Error 1")
            val response2 = ErrorResponse(statusCode = HttpStatus.NOT_FOUND, errorMessage = "Error 2")

            // Then
            assertThat(response1).isNotEqualTo(response2)
        }

        @Test
        @DisplayName("同じ値を持つErrorResponseは同じハッシュコード")
        fun hashCode_sameValues() {
            // Given
            val statusCode = HttpStatus.BAD_REQUEST
            val errorMessage = "Validation error occurred"
            val response1 = ErrorResponse(statusCode = statusCode, errorMessage = errorMessage)
            val response2 = ErrorResponse(statusCode = statusCode, errorMessage = errorMessage)

            // Then
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode())
        }

        @Test
        @DisplayName("copyで別の値でコピー作成できる")
        fun copy_differentValues() {
            // Given
            val originalResponse = ErrorResponse(statusCode = HttpStatus.BAD_REQUEST, errorMessage = "original error")

            // When
            val copiedResponse = originalResponse.copy(errorMessage = "copied error")

            // Then
            assertThat(copiedResponse.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
            assertThat(copiedResponse.errorMessage).isEqualTo("copied error")
        }
    }
}
