package com.example.book_management.repository

import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.book.Book
import com.example.book_management.dto.book.BookId
import com.example.book_management.tables.references.BOOKS
import com.example.book_management.tables.references.BOOK_AUTHORS
import org.jooq.DSLContext
import org.jooq.impl.DSL.row
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

/**
 * 書籍リポジトリインターフェース
 * ドメイン層とインフラ層の境界を定義
 */
@Repository
class JooqBookRepository(private val dsl: DSLContext) : BookRepository {
    /**
     * 複数の書籍を保存する
     */
    override fun insertMany(books: List<Book>) {
        val bookEntities = books.map {
            row(
                it.id.value, it.title.value, it.bookPrice.value, it.publicationStatus.name
            )
        }

        dsl.insertInto(
            BOOKS, BOOKS.ID, BOOKS.TITLE, BOOKS.PRICE, BOOKS.PUBLICATION_STATUS
        ).valuesOfRows(bookEntities).execute()

        books.forEach { book ->
            val bookAuthorsEntity = book.authorIds.map {
                row(book.id.value, it.value)
            }
            dsl.insertInto(
                BOOK_AUTHORS,
                BOOK_AUTHORS.BOOK_ID,
                BOOK_AUTHORS.AUTHOR_ID
            ).valuesOfRows(bookAuthorsEntity)
                .execute()
        }
    }

    /**
     * 書籍を保存する
     */
    override fun insert(book: Book) {
        dsl.insertInto(BOOKS)
            .set(BOOKS.ID, book.id.value)
            .set(BOOKS.TITLE, book.title.value)
            .set(BOOKS.PRICE, book.bookPrice.value)
            .set(BOOKS.PUBLICATION_STATUS, book.publicationStatus.name)
            .execute()

        val bookAuthorsEntity = book.authorIds.map {
            row(book.id.value, it.value)
        }
        dsl.insertInto(
            BOOK_AUTHORS,
            BOOK_AUTHORS.BOOK_ID,
            BOOK_AUTHORS.AUTHOR_ID
        ).valuesOfRows(bookAuthorsEntity)
            .execute()
    }

    override fun existsByTitleAndPrice(book: Book): Boolean {
        return dsl.fetchExists(
            BOOKS,
            BOOKS.TITLE.eq(book.title.value).and(BOOKS.PRICE.eq(book.bookPrice.value))
        )
    }

    /**
     * 書籍を取得する
     */
    override fun findById(id: BookId): Book? {
        val bookRecord = dsl.selectFrom(BOOKS)
            .where(BOOKS.ID.eq(id.value))
            .fetchOne() ?: return null

        val authorIds = dsl
            .select(BOOK_AUTHORS.AUTHOR_ID)
            .from(BOOK_AUTHORS)
            .where(BOOK_AUTHORS.BOOK_ID.eq(id.value))
            .fetch()
            .mapNotNull { it.value1()?.let { value -> AuthorId(value) } }

        return Book.fromRecord(bookRecord, authorIds)
    }

    /**
     * 書籍を更新する（楽観排他制御付き）
     */
    override fun update(
        book: Book,
        expectedUpdatedAt: LocalDateTime
    ): Int {
        val updatedCount = dsl.update(BOOKS)
            .set(BOOKS.TITLE, book.title.value)
            .set(BOOKS.PRICE, book.bookPrice.value)
            .set(BOOKS.PUBLICATION_STATUS, book.publicationStatus.name)
            .set(BOOKS.UPDATED_AT, LocalDateTime.now())
            .where(BOOKS.ID.eq(book.id.value))
            .and(BOOKS.UPDATED_AT.eq(expectedUpdatedAt))
            .execute()

        if (updatedCount <= 0) {
            return updatedCount
        }
        // 既存の著者情報を削除
        dsl.deleteFrom(BOOK_AUTHORS)
            .where(BOOK_AUTHORS.BOOK_ID.eq(book.id.value))
            .execute()

        // 新しい著者情報を挿入
        if (book.authorIds.isNotEmpty()) {
            val bookAuthorsEntity = book.authorIds.map {
                row(book.id.value, it.value)
            }

            dsl.insertInto(
                BOOK_AUTHORS,
                BOOK_AUTHORS.BOOK_ID,
                BOOK_AUTHORS.AUTHOR_ID
            ).valuesOfRows(bookAuthorsEntity)
                .execute()
        }

        return updatedCount
    }

    /**
     * 著者IDで書籍を検索する
     */
    override fun findAllBookByAuthorId(authorId: AuthorId): List<Book> {
        val bookIds = dsl.select(BOOK_AUTHORS.BOOK_ID)
            .from(BOOK_AUTHORS)
            .where(BOOK_AUTHORS.AUTHOR_ID.eq(authorId.value))
            .fetch()
            .map { it.value1() }

        if (bookIds.isEmpty()) {
            return emptyList()
        }

        val bookRecords = dsl.selectFrom(BOOKS)
            .where(BOOKS.ID.`in`(bookIds))
            .fetch()

        return bookRecords.map { bookRecord ->
            val bookAuthorIds = dsl.select(BOOK_AUTHORS.AUTHOR_ID)
                .from(BOOK_AUTHORS)
                .where(BOOK_AUTHORS.BOOK_ID.eq(bookRecord.id))
                .fetch()
                .mapNotNull { it.value1()?.let { value -> AuthorId(value) } }

            Book.fromRecord(bookRecord, bookAuthorIds)
        }
    }
}
