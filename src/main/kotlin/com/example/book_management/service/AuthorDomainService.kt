package com.example.book_management.service

import com.example.book_management.dto.author.Author
import com.example.book_management.dto.author.AuthorName
import com.example.book_management.dto.author.BirthDate
import com.example.book_management.repository.AuthorRepository
import org.springframework.stereotype.Service

/**
 * 著者ドメインサービス
 * 複数のエンティティにまたがるビジネスルールを実装
 */
@Service
class AuthorDomainService(
    private val authorRepository: AuthorRepository
) {
    /**
     * 著者の重複チェック
     * ビジネスルール: 同名で生年月日が同じ著者は存在してはいけない
     */
    fun isDuplicateAuthor(name: AuthorName, birthDate: BirthDate): Boolean {
        return authorRepository.existsByNameAndBirthDate(name, birthDate)
    }
    
    /**
     * 著者更新の妥当性チェック
     * ビジネスルール: 存在する著者のみ更新可能
     */
    fun validateAuthorExists(author: Author): Author {
        return authorRepository.findById(author.id)
            ?: throw IllegalArgumentException("著者が存在しません: ${author.id.value}")
    }
}
