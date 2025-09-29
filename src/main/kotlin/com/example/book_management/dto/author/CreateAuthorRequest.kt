package com.example.book_management.dto.author

import com.example.book_management.dto.book.CreateBookWithAuthorRequest


/**
 * 著者作成リクエストDTO
 */
data class CreateAuthorRequest(
    val name: AuthorName,
    val birthDate: BirthDate,
    val books: List<CreateBookWithAuthorRequest>
)