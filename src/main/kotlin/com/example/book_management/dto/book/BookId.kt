package com.example.book_management.dto.book

import java.util.UUID

/**
 * 書籍IDを表す値オブジェクト
 */
@JvmInline
value class BookId(val value: UUID)
