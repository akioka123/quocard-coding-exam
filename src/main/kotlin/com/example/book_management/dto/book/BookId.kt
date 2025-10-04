package com.example.book_management.dto.book

import java.util.UUID

/**
 * 書籍IDを表す値オブジェクト
 * 
 * UUIDをラップした値オブジェクトで、書籍の一意識別子を表現する。
 * 型安全性を提供し、他のID型との混同を防ぐ。
 */
@JvmInline
value class BookId(val value: UUID)
