package com.example.book_management.repository

import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.book.BookId
import com.example.book_management.tables.references.BOOK_AUTHORS
import org.jooq.DSLContext
import org.jooq.impl.DSL.row
import org.springframework.stereotype.Repository

@Repository
class BookAuthorsRepository(private val dsl: DSLContext) {
    fun insertAuthorBooks(authorId: AuthorId, bookIds: List<BookId>) {
        val bookAuthorsRows = bookIds.map {
            row(it.value, authorId.value)
        }
        dsl.insertInto(
            BOOK_AUTHORS,
            BOOK_AUTHORS.BOOK_ID,
            BOOK_AUTHORS.AUTHOR_ID
        ).valuesOfRows(bookAuthorsRows)
            .execute()
    }

    fun insertBookAuthors(bookId: BookId, authorIds: List<AuthorId>) {
        val bookAuthorsEntity = authorIds.map {
            row(bookId.value, it.value)
        }
        dsl.insertInto(
            BOOK_AUTHORS,
            BOOK_AUTHORS.BOOK_ID,
            BOOK_AUTHORS.AUTHOR_ID
        ).valuesOfRows(bookAuthorsEntity)
            .execute()
    }

    /**
     * 書籍の著者情報を更新する
     * 既存の著者情報を削除して新しい著者情報を挿入する
     */
    fun updateBookAuthors(bookId: BookId, authorIds: List<AuthorId>) {
        // 既存の著者情報を削除
        dsl.deleteFrom(BOOK_AUTHORS)
            .where(BOOK_AUTHORS.BOOK_ID.eq(bookId.value))
            .execute()

        // 新しい著者情報を挿入
        if (authorIds.isNotEmpty()) {
            val bookAuthorsEntity = authorIds.map {
                row(bookId.value, it.value)
            }
            dsl.insertInto(
                BOOK_AUTHORS,
                BOOK_AUTHORS.BOOK_ID,
                BOOK_AUTHORS.AUTHOR_ID
            ).valuesOfRows(bookAuthorsEntity)
                .execute()
        }
    }
}