package com.example.book_management.dto.author

import com.example.book_management.dto.book.BookId


/**
 * 著者作成リクエスト用DTO
 * 
 * 著者作成APIのリクエストボディとして使用されるデータ転送オブジェクト。
 * 著者の基本情報（名前、生年月日）と関連付ける書籍IDのリストを含む。
 * バリデーションは値オブジェクトのinitブロックで実行される。
 */
data class CreateAuthorRequest(
    val name: AuthorName,
    val birthDate: BirthDate,
    val bookIds: List<BookId>
)