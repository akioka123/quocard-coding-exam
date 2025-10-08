package com.example.book_management.repository

import com.example.book_management.dto.author.Author
import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.author.AuthorName
import com.example.book_management.dto.author.BirthDate
import com.example.book_management.dto.book.BookId
import com.example.book_management.tables.references.AUTHORS
import com.example.book_management.tables.references.BOOK_AUTHORS
import org.jooq.DSLContext
import org.jooq.impl.DSL.row
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

/**
 * JOOQを使用した著者リポジトリの実装
 *
 * AuthorRepositoryインターフェースのJOOQ実装。データベースアクセスにJOOQを使用し、
 * 著者エンティティの永続化操作を提供する。著者と書籍の多対多関係も管理し、
 * 楽観排他制御による更新処理も実装している。
 */
@Repository
class JooqAuthorRepository(private val dsl: DSLContext) : AuthorRepository {
    /**
     * 著者を新規作成する
     *
     * 指定された著者情報をAUTHORSテーブルに保存し、同時に指定された書籍IDとの
     * 関連付けをBOOK_AUTHORSテーブルに保存する。著者と書籍の多対多関係を管理する。
     *
     * @param author 作成する著者エンティティ
     * @param bookIds 関連付ける書籍IDのリスト（空の場合は関連付けを行わない）
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

    /**
     * 著者情報を更新する（楽観排他制御付き）
     *
     * AUTHORSテーブルで指定された著者IDの情報を更新する。楽観排他制御により、
     * 期待される更新日時と一致する場合のみ更新を実行する。更新日時は現在時刻に設定される。
     *
     * @param id 更新対象の著者ID
     * @param name 更新する著者名
     * @param birthDate 更新する生年月日
     * @param expectedUpdatedAt 期待される更新日時（楽観排他制御用）
     * @return 更新されたレコード数（0の場合は更新されていない）
     */
    override fun update(
        id: AuthorId,
        name: AuthorName,
        birthDate: BirthDate,
        expectedUpdatedAt: LocalDateTime
    ): Int {
        val updatedCount = dsl.update(AUTHORS)
            .set(AUTHORS.NAME, name.value)
            .set(AUTHORS.BIRTH_DATE, birthDate.value)
            .set(AUTHORS.UPDATED_AT, LocalDateTime.now())
            .where(AUTHORS.ID.eq(id.value))
            .and(AUTHORS.UPDATED_AT.eq(expectedUpdatedAt))
            .execute()

        return updatedCount
    }

    /**
     * 著者IDで著者を検索する
     *
     * AUTHORSテーブルから指定された著者IDに対応するレコードを取得し、
     * Authorエンティティに変換して返す。該当する著者が存在しない場合はnullを返す。
     *
     * @param id 検索対象の著者ID
     * @return 見つかった著者エンティティ、存在しない場合はnull
     */
    override fun findById(id: AuthorId): Author? {
        return dsl.selectFrom(AUTHORS)
            .where(AUTHORS.ID.eq(id.value))
            .fetchOne()
            ?.let { record -> Author.fromRecord(record) }
    }

}
