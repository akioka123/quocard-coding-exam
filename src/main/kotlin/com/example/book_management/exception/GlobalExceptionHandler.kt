package com.example.book_management.exception

import com.example.book_management.dto.response.ErrorResponse
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * グローバルエラーハンドラー
 * 4XX、5XXエラーを統一形式で返却する
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    /**
     * バリデーションエラー（400 Bad Request）
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errorMessage = ex.bindingResult.fieldErrors.joinToString(", ") {
            "${it.field}: ${it.defaultMessage}"
        }

        val response = ErrorResponse(
            statusCode = HttpStatus.BAD_REQUEST,
            errorMessage = errorMessage
        )

        return ResponseEntity.badRequest().body(response)
    }

    /**
     * IllegalArgumentException（400 Bad Request）
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            statusCode = HttpStatus.BAD_REQUEST,
            errorMessage = ex.message ?: "無効なリクエストです"
        )

        return ResponseEntity.badRequest().body(response)
    }

    /**
     * IllegalStateException（400 Bad Request）
     */
    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(ex: IllegalStateException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            statusCode = HttpStatus.BAD_REQUEST,
            errorMessage = ex.message ?: "無効な状態のパラメータが存在します"
        )

        return ResponseEntity.badRequest().body(response)
    }

    @ExceptionHandler(DuplicateKeyException::class)
    fun handleDuplicateKeyException(ex: DuplicateKeyException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            statusCode = HttpStatus.CONFLICT,
            errorMessage = ex.message ?: "重複しています"
        )

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
    }

    /**
     * 楽観排他制御エラー（409 Conflict）
     */
    @ExceptionHandler(OptimisticLockingFailureException::class)
    fun handleOptimisticLockingFailureException(
        ex: OptimisticLockingFailureException
    ): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            statusCode = HttpStatus.CONFLICT,
            errorMessage = ex.message ?: "楽観排他制御エラーが発生しました"
        )

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
    }

    /**
     * その他の例外（500 Internal Server Error）
     */
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR,
            errorMessage = "内部サーバーエラーが発生しました"
        )

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
    }
}
