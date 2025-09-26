package com.example.book_management.dto.book

import com.example.book_management.dto.author.AuthorId

/**
 * 書籍作成リクエストDTO
 */
data class CreateBookRequest(
    val title: BookTitle,
    val bookPrice: BookPrice,
    val author: AuthorId,
    val publicationStatus: PublicationStatus
)