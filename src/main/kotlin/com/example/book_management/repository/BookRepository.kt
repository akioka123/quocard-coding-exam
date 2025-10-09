package com.example.book_management.repository

import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.book.Book
import com.example.book_management.dto.book.BookId
import java.time.LocalDateTime

/**
 * 書籍リポジトリインターフェース
 *
 * 書籍エンティティの永続化操作を定義するリポジトリパターンのインターフェース。
 * ドメイン層とインフラ層の境界を定義し、書籍の作成、更新、検索、存在確認などの
 * 基本的なCRUD操作を提供する。実装はインフラ層で行われる。
 */
interface BookRepository {

    /**
     * 書籍を新規作成する
     *
     * 指定された書籍情報をデータベースに保存し、同時に指定された著者IDとの
     * 関連付けも行う。書籍と著者の多対多関係を管理する。
     *
     * @param book 作成する書籍エンティティ
     */
    fun insert(book: Book)

    /**
     * 書籍IDで書籍を検索する
     *
     * 指定された書籍IDに対応する書籍エンティティを取得する。
     * 該当する書籍が存在しない場合はnullを返す。
     *
     * @param id 検索対象の書籍ID
     * @return 見つかった書籍エンティティ、存在しない場合はnull
     */
    fun findById(id: BookId): Book?

    /**
     * 書籍情報を更新する（楽観排他制御付き）
     *
     * 指定された書籍IDの情報を更新する。楽観排他制御により、
     * 期待される更新日時と一致する場合のみ更新を実行する。
     * 著者との関連付けも更新される。
     *
     * @param book 更新する書籍エンティティ
     * @param expectedUpdatedAt 期待される更新日時（楽観排他制御用）
     * @return 更新されたレコード数（0の場合は更新されていない）
     */
    fun update(
        book: Book,
        expectedUpdatedAt: LocalDateTime
    ): Int

    /**
     * 著者IDで書籍一覧を検索する
     *
     * 指定された著者IDに関連付けられているすべての書籍を取得する。
     * 該当する書籍がない場合は空のリストを返す。
     *
     * @param authorId 検索対象の著者ID
     * @return 該当する書籍エンティティのリスト
     */
    fun findAllBookByAuthorId(authorId: AuthorId): List<Book>
}