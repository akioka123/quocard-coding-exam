package com.example.book_management.dto.book

import java.math.BigDecimal
import java.util.UUID

/**
 * 書籍作成リクエスト用DTO
 * 
 * 書籍作成APIのリクエストボディとして使用されるデータ転送オブジェクト。
 * 書籍の基本情報（タイトル、価格、出版ステータス、著者IDリスト）を含む。
 * バリデーションはコントローラー層で実行される。
 */
data class CreateBookRequest(
    val title: String,
    val price: BigDecimal,
    val publicationStatus: String,
    val authorIds: List<UUID>
)