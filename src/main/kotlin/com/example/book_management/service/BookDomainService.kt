package com.example.book_management.service

import com.example.book_management.dto.book.Book
import com.example.book_management.repository.BookRepository
import org.springframework.stereotype.Service

@Service
class BookDomainService(
    private val bookRepository: BookRepository
) {
    fun isDuplicateBook(book: Book): Boolean {
        return bookRepository.existsByTitleAndPrice(book)
    }

    fun validateBookExists(book: Book): Book {
        return bookRepository.findById(book.id)
            ?: throw IllegalArgumentException("書籍が存在しません: ${book.id.value}")
    }

}