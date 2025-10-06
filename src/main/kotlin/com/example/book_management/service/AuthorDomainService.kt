package com.example.book_management.service

import com.example.book_management.dto.author.Author
import com.example.book_management.repository.AuthorRepository
import org.springframework.stereotype.Service

/**
 * 著者ドメインサービス
 * 
 * 著者エンティティに関する複数のエンティティにまたがるビジネスルールを実装する
 * ドメインサービス。著者の存在確認やバリデーションなどのドメインロジックを
 * 提供し、アプリケーションサービスから利用される。
 */
@Service
class AuthorDomainService(
    private val authorRepository: AuthorRepository
) {
    /**
     * 著者の存在確認を行う
     * 
     * 指定された著者IDでデータベースから著者を検索し、存在する場合は
     * その著者エンティティを返す。存在しない場合はIllegalArgumentExceptionをスローする。
     * ビジネスルールとして、存在する著者のみ更新可能とする。
     * 
     * @param author 確認対象の著者エンティティ（IDが使用される）
     * @return 存在する著者エンティティ
     * @throws IllegalArgumentException 著者が存在しない場合
     */
    fun validateAuthorExists(author: Author): Author {
        return authorRepository.findById(author.id)
            ?: throw IllegalArgumentException("著者が存在しません: ${author.id.value}")
    }
}
