package com.example.book_management.service

import com.example.book_management.dto.author.Author
import com.example.book_management.dto.book.BookId
import com.example.book_management.repository.AuthorRepository
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 著者アプリケーションサービス
 *
 * 著者エンティティのアプリケーション層でのビジネスロジックを実装するサービス。
 * 著者の作成、更新などのトランザクション処理を提供し、ドメインサービスと
 * リポジトリを組み合わせて複雑なビジネス処理を実現する。
 */
@Service
class AuthorService(
    private val authorRepo: AuthorRepository,
    private val authorDomainService: AuthorDomainService
) {
    /**
     * 著者を新規作成する
     *
     * 指定された著者情報と関連する書籍IDリストをデータベースに保存する。
     * トランザクション管理により、著者情報と書籍との関連付けが
     * 原子性を持って実行される。
     *
     * @param author 作成する著者エンティティ
     * @param bookIds 関連付ける書籍IDのリスト
     */
    @Transactional
    fun insert(author: Author, bookIds: List<BookId>) {
        authorRepo.insert(author, bookIds)
    }

    /**
     * 著者情報を更新する（楽観排他制御付き）
     *
     * 指定された著者情報を更新する。まず著者の存在確認を行い、
     * 楽観排他制御により他のユーザーによる更新を検出する。
     * 更新が失敗した場合はOptimisticLockingFailureExceptionをスローする。
     *
     * @param author 更新する著者エンティティ
     * @throws OptimisticLockingFailureException 楽観排他制御エラーが発生した場合
     */
    @Transactional
    fun update(author: Author, bookIds: List<BookId>) {
        val existingAuthor = authorDomainService.validateAuthorExists(author)

        val updatedRows = authorRepo.update(
            author.id,
            author.name,
            author.birthDate,
            bookIds,
            existingAuthor.updatedAt
        )

        if (updatedRows == 0) {
            throw OptimisticLockingFailureException(
                "楽観排他制御エラー: 他のユーザーによって更新されています"
            )
        }
    }
}