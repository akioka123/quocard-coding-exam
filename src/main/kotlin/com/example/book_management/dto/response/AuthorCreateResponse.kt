package com.example.book_management.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 著者作成成功時のレスポンスデータ
 */
data class AuthorCreateResponse(
    @param:JsonProperty("AuthorId")
    val authorId: String
)
