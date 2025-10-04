package com.example.book_management.repository

import com.example.book_management.dto.author.Author
import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.author.AuthorName
import com.example.book_management.dto.author.BirthDate
import com.example.book_management.dto.book.BookId
import java.time.LocalDateTime

interface AuthorRepository {
    fun insert(author: Author, bookIds: List<BookId>)
    fun update(id: AuthorId, name: AuthorName, birthDate: BirthDate, expectedUpdatedAt: LocalDateTime): Int
    fun existsByNameAndBirthDate(name: AuthorName, birthDate: BirthDate): Boolean
    fun findById(id: AuthorId): Author?
}