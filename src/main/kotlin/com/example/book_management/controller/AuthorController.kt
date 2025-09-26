package com.example.book_management.controller

import com.example.book_management.dto.author.AuthorResponse
import com.example.book_management.dto.author.CreateAuthorRequest
import com.example.book_management.dto.author.UpdateAuthorRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 著者コントローラー
 * プレゼンテーション層のHTTPリクエスト/レスポンスを担当
 */
@RestController
@RequestMapping("/api/authors")
class AuthorController {

    /**
     * 著者を登録する
     *
     * @param request 著者登録リクエスト（名前、生年月日、著作リスト）
     * @return 登録成功時は200 OK
     */
    @PostMapping("/create")
    fun createAuthor(@RequestBody request: CreateAuthorRequest): ResponseEntity.BodyBuilder {
        // 著者情報には書籍情報を含む必要がある。
        // 著者は書籍を持たなくてもよい。
        // TODO 著者の登録

        // TODO 著作の登録（既存でなければ）

        // TODO 処理結果の返却
        return ResponseEntity.ok()

    }

    /**
     * 著者を後進する
     *
     * @param request 著者後進リクエスト（名前、生年月日、著作リスト）
     * @return 登録成功時は200 OK
     */
    @PutMapping("/update/{id}")
    fun updateAuthor(@RequestBody request: UpdateAuthorRequest): ResponseEntity.BodyBuilder {
        // 著者情報には書籍情報を含む必要がある。
        // 著者は書籍を持たなくてもよい。
        // TODO 著者の更新

        // TODO 処理結果の返却
        return ResponseEntity.ok()
    }
}