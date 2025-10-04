package com.example.book_management.repository

import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.book.Book
import com.example.book_management.dto.book.BookId
import java.time.LocalDateTime

interface BookRepository {
    fun insertMany(books: List<Book>)
    fun insert(book: Book)
    fun existsByTitleAndPrice(book: Book): Boolean
    fun findById(id: BookId): Book?
    fun update(
        book: Book,
        expectedUpdatedAt: LocalDateTime
    ): Int

    fun findAllBookByAuthorId(authorId: AuthorId): List<Book>
}