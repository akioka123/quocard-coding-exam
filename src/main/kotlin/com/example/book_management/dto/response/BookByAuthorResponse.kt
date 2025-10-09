package com.example.book_management.dto.response

import com.example.book_management.dto.author.AuthorId
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

/**
 * 書籍更新成功時のレスポンス用DTO
 *
 * 書籍更新APIの成功レスポンスとして使用されるデータ転送オブジェクト。
 * 更新された書籍のIDを文字列形式で返却する。
 * JSONプロパティ名は"BookId"として設定される。
 */
data class BookByAuthorResponse(
    @param:JsonProperty("bookId")
    val bookId: String,
    @param:JsonProperty("title")
    val title: String,
    @param:JsonProperty("price")
    val price: BigDecimal,
    @param:JsonProperty("publicationStatus")
    val publicationStatus: String,
    @param:JsonProperty("authorIds")
    val authorIds: List<AuthorId>,
)