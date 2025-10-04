package com.example.book_management.dto.author

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.UUID

/**
 * 著者IDを表す値オブジェクト
 * 
 * UUIDをラップした値オブジェクトで、著者の一意識別子を表現する。
 * JacksonによるJSONシリアライゼーション/デシリアライゼーションをサポートし、
 * 型安全性を提供する。
 */
@JvmInline
value class AuthorId @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
constructor(@get:JsonValue val value: UUID)
