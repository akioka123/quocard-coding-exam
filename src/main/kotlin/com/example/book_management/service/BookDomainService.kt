package com.example.book_management.service

import com.example.book_management.dto.book.Book
import com.example.book_management.repository.BookRepository
import org.springframework.stereotype.Service

/**
 * 書籍ドメインサービス
 * 
 * 書籍エンティティに関する複数のエンティティにまたがるビジネスルールを実装する
 * ドメインサービス。書籍の存在確認やバリデーションなどのドメインロジックを
 * 提供し、アプリケーションサービスから利用される。
 */
@Service
class BookDomainService(
    private val bookRepository: BookRepository
) {
    /**
     * 書籍の存在確認を行う
     * 
     * 指定された書籍IDでデータベースから書籍を検索し、存在する場合は
     * その書籍エンティティを返す。存在しない場合はIllegalArgumentExceptionをスローする。
     * ビジネスルールとして、存在する書籍のみ操作可能とする。
     * 
     * @param book 確認対象の書籍エンティティ（IDが使用される）
     * @return 存在する書籍エンティティ
     * @throws IllegalArgumentException 書籍が存在しない場合
     */
    fun validateBookExists(book: Book): Book {
        return bookRepository.findById(book.id)
            ?: throw IllegalArgumentException("書籍が存在しません: ${book.id.value}")
    }
}