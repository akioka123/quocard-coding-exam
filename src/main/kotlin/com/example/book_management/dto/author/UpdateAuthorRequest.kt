package com.example.book_management.dto.author

/**
 * 著者更新リクエストDTO
 */
data class UpdateAuthorRequest(
    val id: AuthorId,
    val name: AuthorName,
    val birthDate: BirthDate
)