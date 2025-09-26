package com.example.book_management.dto.author

import com.example.book_management.dto.book.CreateBookRequest

/**
 * 著者後進リクエストDTO
 */
data class UpdateAuthorRequest (
    val name: AuthorName,
    val birthDate: BirthDate,
    val books: List<CreateBookRequest>
)