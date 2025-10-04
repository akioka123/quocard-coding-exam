package com.example.book_management.controller

import com.example.book_management.dto.author.Author
import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.author.CreateAuthorRequest
import com.example.book_management.dto.author.UpdateAuthorRequest
import com.example.book_management.dto.response.AuthorCreateResponse
import com.example.book_management.dto.response.AuthorUpdateResponse
import com.example.book_management.dto.response.SuccessResponse
import com.example.book_management.service.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

/**
 * 著者管理のREST APIエンドポイントを提供するコントローラー
 * 
 * 著者の作成、更新、削除などのCRUD操作をHTTPリクエストとして受け取り、
 * ビジネスロジック層のAuthorServiceを呼び出して処理を行い、
 * 適切なHTTPレスポンスを返却する。Spring Bootの@RestControllerアノテーションを使用し、
 * JSON形式でのデータのやり取りを行う。
 */
@RestController
@RequestMapping("/api/authors")
class AuthorController(
    private val authorService: AuthorService
) {
    /**
     * 新しい著者を登録する
     * 
     * リクエストボディから著者情報を受け取り、UUIDでユニークなIDを生成して
     * 著者エンティティを作成し、データベースに保存する。
     * 同時に指定された書籍IDリストとの関連付けも行う。
     * 
     * @param request 著者登録に必要な情報を含むリクエストオブジェクト
     *                - name: 著者名（必須）
     *                - birthDate: 生年月日（必須）
     *                - bookIds: 関連付ける書籍IDのリスト（任意）
     * @return 登録成功時のレスポンス（HTTP 200 OK）
     *         - レスポンスボディには生成された著者IDが含まれる
     * @throws IllegalArgumentException リクエストパラメータが不正な場合
     * @throws DataAccessException データベースアクセスでエラーが発生した場合
     */
    @PostMapping("/create")
    fun createAuthor(
        @RequestBody request: CreateAuthorRequest
    ): ResponseEntity<SuccessResponse<AuthorCreateResponse>> {
        val authorId = AuthorId(UUID.randomUUID())
        val createdAuthor = Author(
            authorId,
            request.name,
            request.birthDate,
            LocalDateTime.now(),
            LocalDateTime.now()
        )

        authorService.insert(createdAuthor, request.bookIds)

        val response = SuccessResponse(
            statusCode = HttpStatus.OK,
            data = AuthorCreateResponse(
                authorId = authorId.value.toString()
            )
        )

        return ResponseEntity.ok(response)
    }

    /**
     * 既存の著者情報を更新する
     * 
     * パスパラメータで指定された著者IDに対応する著者情報を、
     * リクエストボディの情報で更新する。更新日時は現在時刻に自動設定される。
     * 著者と書籍の関連付けは更新されない。
     * 
     * @param request 更新する著者情報を含むリクエストオブジェクト
     *                - name: 更新する著者名（必須）
     *                - birthDate: 更新する生年月日（必須）
     * @param id 更新対象の著者ID（パスパラメータ）
     * @return 更新成功時のレスポンス（HTTP 200 OK）
     *         - レスポンスボディには更新された著者IDが含まれる
     * @throws IllegalArgumentException リクエストパラメータが不正な場合
     * @throws EntityNotFoundException 指定されたIDの著者が存在しない場合
     * @throws DataAccessException データベースアクセスでエラーが発生した場合
     */
    @PutMapping("/update/{id}")
    fun updateAuthor(
        @RequestBody request: UpdateAuthorRequest,
        @PathVariable id: UUID
    ): ResponseEntity<SuccessResponse<AuthorUpdateResponse>> {
        val updatedAuthor = Author(
            AuthorId(id),
            request.name,
            request.birthDate,
            LocalDateTime.now(),
            LocalDateTime.now()
        )
        authorService.update(updatedAuthor)

        val response = SuccessResponse(
            statusCode = HttpStatus.OK,
            data = AuthorUpdateResponse(
                authorId = updatedAuthor.id.value.toString()
            )
        )

        return ResponseEntity.ok(response)
    }
}