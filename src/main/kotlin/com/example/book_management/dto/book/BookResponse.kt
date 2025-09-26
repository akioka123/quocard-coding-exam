package com.example.book_management.dto.book

import com.example.book_management.dto.author.AuthorId

/**
 * 書籍レスポンスDTO
 * 登録された書籍情報を返却するためのデータ転送オブジェクト
 */
data class BookResponse(
    val id: Long,
    val authorIds: List<AuthorId>,
    val title: BookTitle,
    val bookPrice: BookPrice,
    val publicationStatus: PublicationStatus
)
