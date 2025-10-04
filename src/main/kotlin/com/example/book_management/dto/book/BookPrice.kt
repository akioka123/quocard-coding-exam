package com.example.book_management.dto.book

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.math.BigDecimal

/**
 * 書籍価格を表す値オブジェクト
 * 
 * BigDecimalをラップした値オブジェクトで、価格の制約（0以上、12桁以内）を
 * 強制する。JacksonによるJSONシリアライゼーション/デシリアライゼーションを
 * サポートし、複数の数値型からの変換コンストラクタも提供する。
 */
@JvmInline
value class BookPrice @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
constructor(@get:JsonValue val value: BigDecimal) {
    init {
        require(value >= BigDecimal.ZERO) { "価格は0以上でなければなりません。" }
        require(value < BigDecimal("1000000000000.00")) { "価格は整数部12桁以内でなければなりません。" }
    }

    constructor(value: Int) : this(BigDecimal(value))
    constructor(value: Long) : this(BigDecimal(value))
    constructor(value: Double) : this(BigDecimal.valueOf(value))
}
