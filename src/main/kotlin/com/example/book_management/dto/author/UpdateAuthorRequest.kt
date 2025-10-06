package com.example.book_management.dto.author

import com.example.book_management.dto.book.BookId

/**
 * 著者更新リクエスト用DTO
 *
 * 著者更新APIのリクエストボディとして使用されるデータ転送オブジェクト。
 * 更新対象の著者IDと更新する基本情報（名前、生年月日）を含む。
 * バリデーションは値オブジェクトのinitブロックで実行される。
 */
data class UpdateAuthorRequest(
    val name: AuthorName,
    val birthDate: BirthDate,
    val bookIds: List<BookId>
)