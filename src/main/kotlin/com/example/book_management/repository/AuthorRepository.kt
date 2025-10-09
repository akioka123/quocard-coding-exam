package com.example.book_management.repository

import com.example.book_management.dto.author.Author
import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.author.AuthorName
import com.example.book_management.dto.author.BirthDate
import com.example.book_management.dto.book.BookId
import java.time.LocalDateTime

/**
 * 著者リポジトリインターフェース
 *
 * 著者エンティティの永続化操作を定義するリポジトリパターンのインターフェース。
 * ドメイン層とインフラ層の境界を定義し、著者の作成、更新、検索、存在確認などの
 * 基本的なCRUD操作を提供する。実装はインフラ層で行われる。
 */
interface AuthorRepository {
    /**
     * 著者を新規作成する
     *
     * 指定された著者情報をデータベースに保存し、同時に指定された書籍IDとの
     * 関連付けも行う。著者と書籍の多対多関係を管理する。
     *
     * @param author 作成する著者エンティティ
     * @param bookIds 関連付ける書籍IDのリスト
     */
    fun insert(author: Author, bookIds: List<BookId>)

    /**
     * 著者情報を更新する（楽観排他制御付き）
     *
     * 指定された著者IDの情報を更新する。楽観排他制御により、
     * 期待される更新日時と一致する場合のみ更新を実行する。
     *
     * @param id 更新対象の著者ID
     * @param name 更新する著者名
     * @param birthDate 更新する生年月日
     * @param expectedUpdatedAt 期待される更新日時（楽観排他制御用）
     * @return 更新されたレコード数（0の場合は更新されていない）
     */
    fun update(
        id: AuthorId,
        name: AuthorName,
        birthDate: BirthDate,
        expectedUpdatedAt: LocalDateTime
    ): Int

    /**
     * 著者IDで著者を検索する
     *
     * 指定された著者IDに対応する著者エンティティを取得する。
     * 該当する著者が存在しない場合はnullを返す。
     *
     * @param id 検索対象の著者ID
     * @return 見つかった著者エンティティ、存在しない場合はnull
     */
    fun findById(id: AuthorId): Author?
}