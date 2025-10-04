package com.example.book_management.repository

import com.example.book_management.dto.author.Author
import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.author.AuthorName
import com.example.book_management.dto.author.BirthDate
import com.example.book_management.dto.book.BookId
import com.example.book_management.tables.records.AuthorsRecord
import com.example.book_management.tables.references.AUTHORS
import com.example.book_management.tables.references.BOOK_AUTHORS
import org.jooq.DSLContext
import org.jooq.impl.DSL.row
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

/**
 * 著者リポジトリインターフェース
 * ドメイン層とインフラ層の境界を定義
 */
@Repository
class JooqAuthorRepository(private val dsl: DSLContext) : AuthorRepository {
    /**
     * 著者を保存する
     */
    override fun insert(author: Author, bookIds: List<BookId>) {
        dsl.insertInto(AUTHORS)
            .set(AUTHORS.ID, author.id.value)
            .set(AUTHORS.NAME, author.name.value)
            .set(AUTHORS.BIRTH_DATE, author.birthDate.value)
            .execute()

        if (bookIds.isEmpty()) {
            return
        }
        val bookAuthorsRows = bookIds.map {
            row(it.value, author.id.value)
        }
        dsl.insertInto(
            BOOK_AUTHORS,
            BOOK_AUTHORS.BOOK_ID,
            BOOK_AUTHORS.AUTHOR_ID
        ).valuesOfRows(bookAuthorsRows)
            .execute()
    }

    override fun existsByNameAndBirthDate(name: AuthorName, birthDate: BirthDate): Boolean {
        return dsl.fetchExists(
            AUTHORS,
            AUTHORS.NAME.eq(name.value).and(AUTHORS.BIRTH_DATE.eq(birthDate.value))
        )
    }

    /**
     * 著者を更新する（楽観排他制御付き）
     */
    override fun update(id: AuthorId, name: AuthorName, birthDate: BirthDate, expectedUpdatedAt: LocalDateTime): Int {
        return dsl.update(AUTHORS)
            .set(AUTHORS.NAME, name.value)
            .set(AUTHORS.BIRTH_DATE, birthDate.value)
            .set(AUTHORS.UPDATED_AT, LocalDateTime.now())
            .where(AUTHORS.ID.eq(id.value))
            .and(AUTHORS.UPDATED_AT.eq(expectedUpdatedAt))
            .execute()
    }

    /**
     * 著者を取得する
     */
    override fun findById(id: AuthorId): Author? {
        return dsl.selectFrom(AUTHORS)
            .where(AUTHORS.ID.eq(id.value))
            .fetchOne()
            ?.let { record -> Author.fromRecord(record as AuthorsRecord) }
    }

}
