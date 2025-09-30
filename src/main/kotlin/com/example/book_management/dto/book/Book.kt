package com.example.book_management.dto.book

import com.example.book_management.dto.author.AuthorId
import com.example.book_management.tables.records.BooksRecord
import java.time.LocalDateTime

/**
 * 書籍エンティティ
 */
class Book(
    val id: BookId,
    val title: BookTitle,
    val bookPrice: BookPrice,
    val authors: List<AuthorId>,
    val publicationStatus: PublicationStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    init {
        require(authors.isNotEmpty()) { "書籍には最低1人の著者が必要です。" }
    }

    companion object {
        /**
         * データベースレコードからドメインオブジェクトを構築するファクトリーメソッド
         * ドメインロジックとしてデータ整合性を保証する
         */
        fun fromRecord(record: BooksRecord, authors: List<AuthorId> = emptyList()): Book {
            return Book(
                id = BookId(record.id ?: throw IllegalStateException("書籍IDがnullです")),
                title = BookTitle(record.title ?: throw IllegalStateException("書籍タイトルがnullです")),
                bookPrice = BookPrice(record.price ?: throw IllegalStateException("価格がnullです")),
                authors = authors,
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
