package com.example.book_management.service

import com.example.book_management.dto.author.Author
import com.example.book_management.dto.book.BookId
import com.example.book_management.repository.AuthorRepository
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthorService(
    private val authorRepo: AuthorRepository,
    private val authorDomainService: AuthorDomainService
) {
    @Transactional
    fun insert(author: Author, bookIds: List<BookId>) {
        authorRepo.insert(author, bookIds)
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