package com.example.book_management.controller

import com.example.book_management.dto.author.Author
import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.author.CreateAuthorRequest
import com.example.book_management.dto.author.UpdateAuthorRequest
import com.example.book_management.dto.book.Book
import com.example.book_management.dto.book.BookId
import com.example.book_management.dto.response.AuthorCreateResponse
import com.example.book_management.dto.response.SuccessResponse
import com.example.book_management.service.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * 著者コントローラー
 * プレゼンテーション層のHTTPリクエスト/レスポンスを担当
 */
@RestController
@RequestMapping("/api/authors")
class AuthorController(
    private val authorService: AuthorService
) {
    /**
     * 著者を登録する
     *
     * @param request 著者登録リクエスト（名前、生年月日、著作リスト）
     * @return 登録成功時は200 OK
     */
    @PostMapping("/create")
    fun createAuthor(@RequestBody request: CreateAuthorRequest): ResponseEntity<SuccessResponse<AuthorCreateResponse>> {
        val authorId = AuthorId(UUID.randomUUID())
        val author = Author(authorId, request.name, request.birthDate)

        val books: List<Book> = request.books.map {
            Book(
                BookId(UUID.randomUUID()),
                it.title,
                it.bookPrice,
                listOf(authorId),
                it.publicationStatus
            )
        }

        authorService.insert(author, books)

        val response = SuccessResponse(
            statusCode = HttpStatus.OK,
            data = AuthorCreateResponse(
                authorId = authorId.value.toString(),
                bookIds = books.map { it.id.value.toString() })
        )

        return ResponseEntity.ok(response)
    }

    /**
     * 著者を更新する
     *
     * @param request 著者後進リクエスト（ID、名前、生年月日、著作リスト）
     * @return 登録成功時は200 OK
     */
    @PutMapping("/update/{id}")
    fun updateAuthor(@RequestBody request: UpdateAuthorRequest): ResponseEntity<SuccessResponse<String>> {
        // TODO 著者の更新

        val response = SuccessResponse(
            statusCode = HttpStatus.OK,
            data = "著者が正常に更新されました"
        )

        return ResponseEntity.ok(response)
    }
}