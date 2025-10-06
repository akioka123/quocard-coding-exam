package com.example.book_management.controller

import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.book.*
import com.example.book_management.dto.response.BookCreateResponse
import com.example.book_management.dto.response.BookUpdateResponse
import com.example.book_management.dto.response.SuccessResponse
import com.example.book_management.service.BookService
import org.jooq.exception.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

/**
 * 書籍管理のREST APIエンドポイントを提供するコントローラー
 *
 * 書籍の作成、更新、検索などのCRUD操作をHTTPリクエストとして受け取り、
 * ビジネスロジック層のBookServiceを呼び出して処理を行い、
 * 適切なHTTPレスポンスを返却する。Spring Bootの@RestControllerアノテーションを使用し、
 * JSON形式でのデータのやり取りを行う。
 */
@RestController
@RequestMapping("/api/books")
class BookController(
    private val bookService: BookService
) {
    /**
     * 新しい書籍を登録する
     *
     * リクエストボディから書籍情報を受け取り、UUIDでユニークなIDを生成して
     * 書籍エンティティを作成し、データベースに保存する。
     * 指定された著者IDリストとの関連付けも同時に行う。
     *
     * @param request 書籍登録に必要な情報を含むリクエストオブジェクト
     *                - title: 書籍タイトル（必須）
     *                - price: 書籍価格（必須、正の数値）
     *                - publicationStatus: 出版ステータス（必須、PUBLISHED/UNPUBLISHED）
     *                - authorIds: 関連付ける著者IDのリスト（必須、空でない）
     * @return 登録成功時のレスポンス（HTTP 200 OK）
     *         - レスポンスボディには生成された書籍IDが含まれる
     * @throws IllegalArgumentException リクエストパラメータが不正な場合
     * @throws DataAccessException データベースアクセスでエラーが発生した場合
     */
    @PostMapping("/")
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

    /**
     * 既存の書籍情報を更新する
     *
     * パスパラメータで指定された書籍IDに対応する書籍情報を、
     * リクエストボディの情報で更新する。更新日時は現在時刻に自動設定される。
     * 書籍と著者の関連付けも更新される。
     *
     * @param request 更新する書籍情報を含むリクエストオブジェクト
     *                - title: 更新する書籍タイトル（必須）
     *                - price: 更新する書籍価格（必須、正の数値）
     *                - publicationStatus: 更新する出版ステータス（必須）
     *                - authorIds: 関連付ける著者IDのリスト（必須、空でない）
     * @param id 更新対象の書籍ID（パスパラメータ）
     * @return 更新成功時のレスポンス（HTTP 200 OK）
     *         - レスポンスボディには更新された書籍IDが含まれる
     * @throws IllegalArgumentException リクエストパラメータが不正な場合
     * @throws IllegalArgumentException 指定されたIDの書籍が存在しない場合
     * @throws DataAccessException データベースアクセスでエラーが発生した場合
     */
    @PutMapping("/{id}")
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

    /**
     * 指定された著者IDに関連する書籍一覧を取得する
     *
     * クエリパラメータで指定された著者IDに関連付けられている
     * すべての書籍を検索し、リスト形式で返却する。
     * 関連付けられていない場合は空のリストを返す。
     *
     * @param authorId 検索対象の著者ID（クエリパラメータ）
     * @return 検索成功時のレスポンス（HTTP 200 OK）
     *         - レスポンスボディには該当する書籍のリストが含まれる
     *         - 該当する書籍がない場合は空のリストを返す
     * @throws IllegalArgumentException 著者IDが不正な場合
     * @throws DataAccessException データベースアクセスでエラーが発生した場合
     */
    @GetMapping("/get")
    fun findByAuthorId(
        @RequestParam("authorId") authorId: UUID
    ): ResponseEntity<SuccessResponse<List<Book>>> {
        val books = bookService.findByAuthorId(AuthorId(authorId))

        val response = SuccessResponse(
            statusCode = HttpStatus.OK,
            data = books
        )

        return ResponseEntity.ok(response)
    }
}