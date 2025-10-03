package com.example.book_management.dto.author

import com.example.book_management.tables.records.AuthorsRecord
import java.time.LocalDateTime

/**
 * 著者エンティティ
 */
data class Author(
    val id: AuthorId,
    val name: AuthorName,
    val birthDate: BirthDate,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        /**
         * データベースレコードからドメインオブジェクトを構築するファクトリーメソッド
         * ドメインロジックとしてデータ整合性を保証する
         */
        fun fromRecord(record: AuthorsRecord): Author {
            return Author(
                id = AuthorId(record.id ?: throw IllegalStateException("著者IDがnullです")),
                name = AuthorName(record.name ?: throw IllegalStateException("著者名がnullです")),
                birthDate = BirthDate(record.birthDate ?: throw IllegalStateException("生年月日がnullです")),
                createdAt = record.createdAt ?: throw IllegalStateException("作成日時がnullです"),
                updatedAt = record.updatedAt ?: throw IllegalStateException("更新日時がnullです")
            )
        }
    }
}
