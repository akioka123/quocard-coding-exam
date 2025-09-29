package com.example.book_management.dto.book

import com.example.book_management.dto.author.AuthorId

/**
 * 書籍エンティティ
 */
class Book(
    val id: BookId,
    val title: BookTitle,
    val bookPrice: BookPrice,
    val authors: List<AuthorId>,
    private var _publicationStatus: PublicationStatus
) {
    init {
        require(authors.isNotEmpty()) { "書籍には最低1人の著者が必要です。" }
    }

    val publicationStatus: PublicationStatus get() = _publicationStatus
}
