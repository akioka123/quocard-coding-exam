package com.example.book_management.dto.book

import com.example.book_management.dto.author.AuthorId
import com.example.book_management.tables.records.BooksRecord
import java.time.LocalDateTime

/**
 * 書籍ドメインエンティティ
 * 
 * 書籍の基本情報（ID、タイトル、価格、著者リスト、出版ステータス）と
 * メタデータ（作成日時、更新日時）を保持するドメインオブジェクト。
 * データベースレコードからの変換機能も提供し、ドメインロジックとして
 * データ整合性を保証する。
 */
data class Book(
    val id: BookId,
    val title: BookTitle,
    val bookPrice: BookPrice,
    val authorIds: List<AuthorId>,
    val publicationStatus: PublicationStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    init {
        require(authorIds.isNotEmpty()) { "書籍には最低1人の著者が必要です。" }
    }

    companion object {
        /**
         * データベースレコードから書籍ドメインオブジェクトを構築するファクトリーメソッド
         * 
         * JOOQで生成されたBooksRecordからBookエンティティを作成する。
         * 必須フィールドがnullの場合はIllegalStateExceptionをスローして
         * データ整合性を保証する。著者IDリストは別途設定可能。
         * 
         * @param record データベースから取得した書籍レコード
         * @param authorIds 関連付ける著者IDのリスト（デフォルト: 空リスト）
         * @return 構築された書籍エンティティ
         * @throws IllegalStateException 必須フィールドがnullの場合
         */
        fun fromRecord(record: BooksRecord, authorIds: List<AuthorId> = emptyList()): Book {
            return Book(
                id = BookId(record.id ?: throw IllegalStateException("書籍IDがnullです")),
                title = BookTitle(record.title ?: throw IllegalStateException("書籍タイトルがnullです")),
                bookPrice = BookPrice(record.price ?: throw IllegalStateException("価格がnullです")),
                authorIds = authorIds,
                publicationStatus = PublicationStatus.fromValue(
                    record.publicationStatus ?: throw IllegalStateException(
                        "出版状況がnullです"
                    )
                ),
                createdAt = record.createdAt ?: throw IllegalStateException("作成日時がnullです"),
                updatedAt = record.updatedAt ?: throw IllegalStateException("更新日時がnullです")
            )
        }
    }
}
