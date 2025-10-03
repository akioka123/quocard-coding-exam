package com.example.book_management.dto.book

import com.example.book_management.dto.author.AuthorId
import java.util.*

/**
 * 書籍レスポンスDTO
 * 登録された書籍情報を返却するためのデータ転送オブジェクト
 */
data class BookResponse(
    val id: UUID,
    val authorIds: List<AuthorId>,
    val title: BookTitle,
    val bookPrice: BookPrice,
    val publicationStatus: PublicationStatus
)
