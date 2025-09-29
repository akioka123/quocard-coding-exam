package com.example.book_management.dto.book

/**
 * 書籍作成リクエストDTO
 */
data class CreateBookWithAuthorRequest(
    val title: BookTitle,
    val bookPrice: BookPrice,
    val publicationStatus: PublicationStatus
)