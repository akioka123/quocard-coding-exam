package com.example.book_management.repository

import org.jooq.impl.DSL.row
import com.example.book_management.dto.book.Book
import com.example.book_management.tables.references.BOOKS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

/**
 * 書籍リポジトリインターフェース
 * ドメイン層とインフラ層の境界を定義
 */
@Repository
class BookRepository(private val dsl: DSLContext) {
    /**
     * 書籍を保存する
     */
    fun insert(books: List<Book>) {
        val bookEntities = books.map {
            row(
                it.id.value,
                it.title.value,
                it.bookPrice.value,
                it.publicationStatus.name
            )
        }

        dsl.insertInto(
            BOOKS,
            BOOKS.ID,
            BOOKS.TITLE,
            BOOKS.PRICE,
            BOOKS.PUBLICATION_STATUS
        )
            .valuesOfRows(bookEntities)
            .execute()
    }


//        dsl.insertInto(BOOKS)
//            .set(BOOKS.ID, book.id.value)
//            .set(BOOKS.TITLE, book.title.value)
//            .set(BOOKS.PRICE, book.bookPrice.value)
//            .set(BOOKS.PUBLICATION_STATUS, book.publicationStatus.name)

    /**
     * 著者IDで書籍を検索する
     */

}
