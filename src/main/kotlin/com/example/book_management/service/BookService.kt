package com.example.book_management.service

import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.book.Book
import com.example.book_management.repository.BookRepository
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(
    private val bookRepo: BookRepository,
    private val bookDomainService: BookDomainService
) {
    @Transactional
    fun insert(book: Book) {
        bookRepo.insert(book)
    }

    @Transactional
    fun update(book: Book) {
        // 書籍が存在するかどうかを確認
        val existingBook = bookDomainService.validateBookExists(book)

        // 出版状況の状態遷移を検証
        existingBook.publicationStatus.transitionTo(book.publicationStatus)

        // 存在する書籍情報を送信されてきた書籍情報で更新する
        val updatedRows = bookRepo.update(
            book,
            existingBook.updatedAt
        )

        if (updatedRows == 0) {
            throw OptimisticLockingFailureException(
                "楽観排他制御エラー: 他のユーザーによって更新されています"
            )
        }
    }

    fun findByAuthorId(authorId: AuthorId): List<Book> {
        return bookRepo.findAllBookByAuthorId(authorId)
    }
}