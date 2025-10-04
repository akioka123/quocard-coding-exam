package com.example.book_management.dto.author

import com.example.book_management.tables.records.AuthorsRecord
import java.time.LocalDateTime

/**
 * 著者ドメインエンティティ
 * 
 * 著者の基本情報（ID、名前、生年月日）とメタデータ（作成日時、更新日時）を
 * 保持するドメインオブジェクト。データベースレコードからの変換機能も提供し、
 * ドメインロジックとしてデータ整合性を保証する。
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
         * データベースレコードから著者ドメインオブジェクトを構築するファクトリーメソッド
         * 
         * JOOQで生成されたAuthorsRecordからAuthorエンティティを作成する。
         * 必須フィールドがnullの場合はIllegalStateExceptionをスローして
         * データ整合性を保証する。
         * 
         * @param record データベースから取得した著者レコード
         * @return 構築された著者エンティティ
         * @throws IllegalStateException 必須フィールドがnullの場合
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
