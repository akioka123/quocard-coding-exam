package com.example.book_management.dto.author

import com.example.book_management.dto.book.BookResponse

/**
 * 著者レスポンスDTO
 * 登録された著者情報を返却するためのデータ転送オブジェクト
 */
data class AuthorResponse(
    val id: AuthorId,
    val name: AuthorName,
    val birthDate: BirthDate,
    val books: List<BookResponse>
)