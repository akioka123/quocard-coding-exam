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
 * JOOQを使用した書籍リポジトリの実装
 *
 * BookRepositoryインターフェースのJOOQ実装。データベースアクセスにJOOQを使用し、
 * 書籍エンティティの永続化操作を提供する。書籍と著者の多対多関係も管理し、
 * 楽観排他制御による更新処理も実装している。
 */
@Repository
class JooqBookRepository(private val dsl: DSLContext) : BookRepository {

    /**
     * 書籍を新規作成する
     *
     * 指定された書籍情報をBOOKSテーブルに保存し、同時に指定された著者IDとの
     * 関連付けをBOOK_AUTHORSテーブルに保存する。書籍と著者の多対多関係を管理する。
     *
     * @param book 作成する書籍エンティティ
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

    /**
     * 書籍IDで書籍を検索する
     *
     * BOOKSテーブルから指定された書籍IDに対応するレコードを取得し、
     * 関連する著者IDもBOOK_AUTHORSテーブルから取得してBookエンティティに変換して返す。
     * 該当する書籍が存在しない場合はnullを返す。
     *
     * @param id 検索対象の書籍ID
     * @return 見つかった書籍エンティティ、存在しない場合はnull
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
     * 書籍情報を更新する（楽観排他制御付き）
     *
     * BOOKSテーブルで指定された書籍IDの情報を更新する。楽観排他制御により、
     * 期待される更新日時と一致する場合のみ更新を実行する。更新日時は現在時刻に設定される。
     * 既存の著者との関連付けを削除し、新しい関連付けを作成する。
     *
     * @param book 更新する書籍エンティティ
     * @param expectedUpdatedAt 期待される更新日時（楽観排他制御用）
     * @return 更新されたレコード数（0の場合は更新されていない）
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
     * 著者IDで書籍一覧を検索する
     *
     * BOOK_AUTHORSテーブルから指定された著者IDに関連付けられている書籍IDを取得し、
     * それらの書籍情報をBOOKSテーブルから取得してBookエンティティのリストとして返す。
     * 各書籍の関連著者情報も取得する。該当する書籍がない場合は空のリストを返す。
     *
     * @param authorId 検索対象の著者ID
     * @return 該当する書籍エンティティのリスト
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
