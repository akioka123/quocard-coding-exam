package com.example.book_management.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus

/**
 * エラーレスポンス用のDTO
 */
data class ErrorResponse(
    @param:JsonProperty("statusCode")
    val statusCode: HttpStatus,
    @param:JsonProperty("errorMessage")
    val errorMessage: String
)
