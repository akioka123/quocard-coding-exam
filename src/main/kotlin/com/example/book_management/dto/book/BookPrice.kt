package com.example.book_management.dto.book

import java.math.BigDecimal

/**
 * 価格を表す値オブジェクト
 */
@JvmInline
value class BookPrice(val value: BigDecimal) {
    init {
        require(value >= BigDecimal.ZERO) { "価格は0以上でなければなりません。" }
    }
    
    constructor(value: Int) : this(BigDecimal(value))
    constructor(value: Long) : this(BigDecimal(value))
    constructor(value: Double) : this(BigDecimal.valueOf(value))
}
