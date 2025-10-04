package com.example.book_management.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 書籍作成成功時のレスポンス用DTO
 * 
 * 書籍作成APIの成功レスポンスとして使用されるデータ転送オブジェクト。
 * 作成された書籍のIDを文字列形式で返却する。
 * JSONプロパティ名は"BookId"として設定される。
 */
data class BookCreateResponse (
    @param:JsonProperty("BookId")
    val bookId: String,
)