package com.example.book_management.dto.book

import java.math.BigDecimal
import java.util.UUID

data class BookUpdateRequest(
    val title: String,
    val price: BigDecimal,
    val publicationStatus: String,
    val authorIds: List<UUID>
)