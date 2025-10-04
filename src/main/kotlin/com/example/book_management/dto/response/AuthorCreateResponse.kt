package com.example.book_management.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 著者作成成功時のレスポンス用DTO
 * 
 * 著者作成APIの成功レスポンスとして使用されるデータ転送オブジェクト。
 * 作成された著者のIDを文字列形式で返却する。
 * JSONプロパティ名は"AuthorId"として設定される。
 */
data class AuthorCreateResponse(
    @param:JsonProperty("AuthorId")
    val authorId: String
)
