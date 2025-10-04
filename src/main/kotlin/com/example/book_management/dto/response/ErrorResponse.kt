package com.example.book_management.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus

/**
 * エラーレスポンス用DTO
 * 
 * APIエラー発生時のレスポンスとして使用されるデータ転送オブジェクト。
 * HTTPステータスコードとエラーメッセージを含み、クライアント側での
 * エラーハンドリングに必要な情報を提供する。
 */
data class ErrorResponse(
    @param:JsonProperty("statusCode")
    val statusCode: HttpStatus,
    @param:JsonProperty("errorMessage")
    val errorMessage: String
)
