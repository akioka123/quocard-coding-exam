package com.example.book_management.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 著者更新成功時のレスポンスデータ
 */
data class AuthorUpdateResponse(
    @param:JsonProperty("AuthorId")
    val authorId: String,
)
