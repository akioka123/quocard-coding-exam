package com.example.book_management.service

import com.example.book_management.dto.author.Author
import com.example.book_management.dto.book.Book
import com.example.book_management.repository.AuthorRepository
import com.example.book_management.repository.BookAuthorsRepository
import com.example.book_management.repository.BookRepository
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthorService(
    private val authorRepo: AuthorRepository,
    private val bookRepo: BookRepository,
    private val bookAuthorsRepo: BookAuthorsRepository,
    private val authorDomainService: AuthorDomainService
) {
    @Transactional
    fun insert(author: Author, books: List<Book>) {
        if (authorDomainService.isDuplicateAuthor(author.name, author.birthDate)) {
            throw DuplicateKeyException("同名で生年月日が同じ著者が存在します。")
        }

        authorRepo.insert(author.id, author.name, author.birthDate)
        bookRepo.insertMany(books)
        bookAuthorsRepo.insertAuthorBooks(author.id, books.map { it.id })
    }

    @Transactional
    fun update(author: Author) {
        val existingAuthor = authorDomainService.validateAuthorExists(author)

        val updatedRows = authorRepo.update(
            author.id,
            author.name,
            author.birthDate,
            existingAuthor.updatedAt
        )

        if (updatedRows == 0) {
            throw OptimisticLockingFailureException(
                "楽観排他制御エラー: 他のユーザーによって更新されています"
            )
        }
    }
}