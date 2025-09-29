package com.example.book_management.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus

/**
 * 成功レスポンス用のDTO
 */
data class SuccessResponse<T>(
    @param:JsonProperty("statusCode")
    val statusCode: HttpStatus,
    @param:JsonProperty("data")
    val data: T
)
