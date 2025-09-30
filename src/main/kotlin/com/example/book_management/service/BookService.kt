package com.example.book_management.service

import com.example.book_management.dto.book.Book
import com.example.book_management.repository.BookAuthorsRepository
import com.example.book_management.repository.BookRepository
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(
    private val bookRepo: BookRepository,
    private val bookAuthorsRepo: BookAuthorsRepository,
    private val bookDomainService: BookDomainService
) {
    @Transactional
    fun insert(book: Book) {
        if (bookDomainService.isDuplicateBook(book)) {
            throw DuplicateKeyException("タイトルと値段が同じ書籍が存在します。")
        }

        bookRepo.insert(book)
        bookAuthorsRepo.insertBookAuthors(book.id, book.authors)
    }

    @Transactional
    fun update(book: Book) {
        // 書籍が存在するかどうかを確認
        val existingBook = bookDomainService.validateBookExists(book)

        // 出版状況の状態遷移を検証
        existingBook.publicationStatus.transitionTo(book.publicationStatus)

        // 存在する書籍情報を送信されてきた書籍情報で更新する
        val updatedRows = bookRepo.update(
            book.id,
            book.title,
            book.bookPrice,
            book.publicationStatus,
            existingBook.updatedAt
        )

        if (updatedRows == 0) {
            throw OptimisticLockingFailureException(
                "楽観排他制御エラー: 他のユーザーによって更新されています"
            )
        }

        // その書籍に関連する中間テーブルの著者IDも更新する
        bookAuthorsRepo.updateBookAuthors(book.id, book.authors)
    }
}