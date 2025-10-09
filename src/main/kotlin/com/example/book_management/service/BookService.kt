package com.example.book_management.service

import com.example.book_management.dto.author.AuthorId
import com.example.book_management.dto.book.Book
import com.example.book_management.repository.BookRepository
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 書籍アプリケーションサービス
 * 
 * 書籍エンティティのアプリケーション層でのビジネスロジックを実装するサービス。
 * 書籍の作成、更新、検索などのトランザクション処理を提供し、ドメインサービスと
 * リポジトリを組み合わせて複雑なビジネス処理を実現する。
 */
@Service
class BookService(
    private val bookRepo: BookRepository,
    private val bookDomainService: BookDomainService
) {
    /**
     * 書籍を新規作成する
     * 
     * 指定された書籍情報をデータベースに保存する。
     * トランザクション管理により、書籍情報と著者との関連付けが
     * 原子性を持って実行される。
     * 
     * @param book 作成する書籍エンティティ
     */
    @Transactional
    fun insert(book: Book) {
        bookRepo.insert(book)
    }

    /**
     * 書籍情報を更新する（楽観排他制御・状態遷移検証付き）
     * 
     * 指定された書籍情報を更新する。まず書籍の存在確認を行い、
     * 出版ステータスの状態遷移ルールを検証する。その後、楽観排他制御により
     * 他のユーザーによる更新を検出する。更新が失敗した場合は
     * OptimisticLockingFailureExceptionをスローする。
     * 
     * @param book 更新する書籍エンティティ
     * @throws OptimisticLockingFailureException 楽観排他制御エラーが発生した場合
     * @throws IllegalStateException 無効な状態遷移が発生した場合
     */
    @Transactional
    fun update(book: Book) {
        // 書籍が存在するかどうかを確認
        val existingBook = bookDomainService.validateBookExists(book)

        // 出版状況の状態遷移を検証
        existingBook.publicationStatus.transitionTo(book.publicationStatus)

        // 存在する書籍情報を送信されてきた書籍情報で更新する
        val updatedRows = bookRepo.update(
            book,
            existingBook.updatedAt
        )

        if (updatedRows == 0) {
            throw OptimisticLockingFailureException(
                "楽観排他制御エラー: 他のユーザーによって更新されています"
            )
        }
    }

    /**
     * 著者IDで書籍一覧を検索する
     * 
     * 指定された著者IDに関連付けられているすべての書籍を取得する。
     * 該当する書籍がない場合は空のリストを返す。
     * 
     * @param authorId 検索対象の著者ID
     * @return 該当する書籍エンティティのリスト
     */
    fun findByAuthorId(authorId: AuthorId): List<Book> {
        return bookRepo.findAllBookByAuthorId(authorId)
    }
}