package com.example.book_management.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 書籍更新成功時のレスポンス用DTO
 * 
 * 書籍更新APIの成功レスポンスとして使用されるデータ転送オブジェクト。
 * 更新された書籍のIDを文字列形式で返却する。
 * JSONプロパティ名は"BookId"として設定される。
 */
data class BookUpdateResponse (
    @param:JsonProperty("BookId")
    val bookId: String,
)