package com.example.book_management.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 著者更新成功時のレスポンス用DTO
 * 
 * 著者更新APIの成功レスポンスとして使用されるデータ転送オブジェクト。
 * 更新された著者のIDを文字列形式で返却する。
 * JSONプロパティ名は"AuthorId"として設定される。
 */
data class AuthorUpdateResponse(
    @param:JsonProperty("AuthorId")
    val authorId: String,
)
