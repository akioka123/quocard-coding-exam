package com.example.book_management.service

import com.example.book_management.dto.author.Author
import com.example.book_management.dto.book.Book
import com.example.book_management.repository.AuthorRepository
import com.example.book_management.repository.BookAuthorsRepository
import com.example.book_management.repository.BookRepository
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthorService(
    private val authorRepo: AuthorRepository,
    private val bookRepo: BookRepository,
    private val bookAuthorsRepo: BookAuthorsRepository
) {
    @Transactional
    fun insert(author: Author, books: List<Book>) {
        if (authorRepo.existsByNameAndBirthDate(author.name, author.birthDate)) {
            throw DuplicateKeyException("同名で生年月日が同じ著者が存在します。")
        }

        authorRepo.insert(author.id, author.name, author.birthDate)
        bookRepo.insert(books)
        bookAuthorsRepo.insertAuthorBooks(author.id, books.map { it.id })
    }
}