package com.example.book_management.controller

import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.book.Book
import com.example.book_management.dto.book.BookId
import com.example.book_management.dto.book.BookPrice
import com.example.book_management.dto.book.BookTitle
import com.example.book_management.dto.book.BookUpdateRequest
import com.example.book_management.dto.book.CreateBookRequest
import com.example.book_management.dto.book.PublicationStatus
import com.example.book_management.dto.response.BookCreateResponse
import com.example.book_management.dto.response.BookUpdateResponse
import com.example.book_management.dto.response.SuccessResponse
import com.example.book_management.service.BookService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.UUID

/**
 * 書籍コントローラー
 * プレゼンテーション層のHTTPリクエスト/レスポンスを担当
 */
@RestController
@RequestMapping("/api/books")
class BookController(
    private val bookService: BookService
) {
    /**
     * 書籍を登録する
     *
     * @param request 書籍登録リクエスト（タイトル、価格、出版未/済、著者IDリスト）
     * @return 登録成功時は200 OK
     */
    @PostMapping("/create")
    fun createBook(
        @RequestBody request: CreateBookRequest
    ): ResponseEntity<SuccessResponse<BookCreateResponse>> {
        val bookId = BookId(UUID.randomUUID())
        val createdBook = Book(
            bookId,
            BookTitle(request.title),
            BookPrice(request.price),
            request.authorIds.map { AuthorId(it) },
            PublicationStatus.fromValue(request.publicationStatus),
            LocalDateTime.now(),
            LocalDateTime.now()
        )

        bookService.insert(createdBook)

        val response = SuccessResponse(
            statusCode = HttpStatus.OK,
            data = BookCreateResponse(
                bookId = bookId.value.toString()
            )
        )

        return ResponseEntity.ok(response)
    }

    @PutMapping("/update/{id}")
    fun updateBook(
        @RequestBody request: BookUpdateRequest,
        @PathVariable id: UUID
    ): ResponseEntity<SuccessResponse<BookUpdateResponse>> {
        val updatedBook = Book(
            BookId(id),
            BookTitle(request.title),
            BookPrice(request.price),
            request.authorIds.map { AuthorId(it) },
            PublicationStatus.fromValue(request.publicationStatus),
            LocalDateTime.now(),
            LocalDateTime.now()
        )

        bookService.update(updatedBook)

        val response = SuccessResponse(
            statusCode = HttpStatus.OK,
            data = BookUpdateResponse(
                bookId = id.toString()
            )
        )

        return ResponseEntity.ok(response)
    }
}