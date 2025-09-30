package com.example.book_management.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class BookCreateResponse (
    @param:JsonProperty("BookId")
    val bookId: String,
)