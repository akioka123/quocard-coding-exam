package com.example.book_management.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus

/**
 * 成功レスポンス用DTO
 * 
 * API成功時のレスポンスとして使用される汎用的なデータ転送オブジェクト。
 * HTTPステータスコードと任意の型のデータを含み、統一されたレスポンス形式を
 * 提供する。ジェネリック型Tにより様々なデータ型に対応可能。
 */
data class SuccessResponse<T>(
    @param:JsonProperty("statusCode")
    val statusCode: HttpStatus,
    @param:JsonProperty("data")
    val data: T
)
