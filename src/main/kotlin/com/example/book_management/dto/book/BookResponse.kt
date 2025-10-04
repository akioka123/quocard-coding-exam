package com.example.book_management.dto.book

import com.example.book_management.dto.author.AuthorId
import java.util.*

/**
 * 書籍情報のレスポンス用DTO
 * 
 * APIレスポンスとして書籍情報を返却するためのデータ転送オブジェクト。
 * 書籍の基本情報と関連する著者IDのリストを含む。
 * クライアント側での表示や処理に適した形式でデータを提供する。
 */
data class BookResponse(
    val id: UUID,
    val authorIds: List<AuthorId>,
    val title: BookTitle,
    val bookPrice: BookPrice,
    val publicationStatus: PublicationStatus
)
