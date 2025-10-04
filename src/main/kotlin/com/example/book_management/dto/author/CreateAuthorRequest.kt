package com.example.book_management.dto.author

import com.example.book_management.dto.book.BookId


/**
 * 著者作成リクエストDTO
 */
data class CreateAuthorRequest(
    val name: AuthorName,
    val birthDate: BirthDate,
    val bookIds: List<BookId>
)