package com.example.book_management.repository

import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.book.BookId
import com.example.book_management.tables.references.BOOK_AUTHORS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.jooq.impl.DSL.row

@Repository
class BookAuthorsRepository(private val dsl: DSLContext) {
    fun insertAuthorBooks(authorId: AuthorId, bookIds: List<BookId>) {
        val bookAuthorsEntity = bookIds.map {
            row(it.value, authorId.value)
        }
        dsl.insertInto(
            BOOK_AUTHORS,
            BOOK_AUTHORS.BOOK_ID,
            BOOK_AUTHORS.AUTHOR_ID
        ).valuesOfRows(bookAuthorsEntity)
            .execute()
    }
}