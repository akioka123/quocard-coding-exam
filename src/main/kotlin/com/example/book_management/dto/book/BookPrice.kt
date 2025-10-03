package com.example.book_management.dto.book

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.math.BigDecimal

/**
 * 価格を表す値オブジェクト
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
