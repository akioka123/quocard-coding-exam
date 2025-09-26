package com.example.book_management.dto.book

import com.example.book_management.dto.author.Author

/**
 * 書籍エンティティ
 */
class Book(
    val id: BookId,
    val title: BookTitle,
    val bookPrice: BookPrice,
    val authors: List<Author>,
    private var _publicationStatus: PublicationStatus
) {
    init {
        require(authors.isNotEmpty()) { "書籍には最低1人の著者が必要です。" }
    }

    val publicationStatus: PublicationStatus get() = _publicationStatus

    /**
     * 出版状況を変更する
     * 出版済みから未出版への変更は不可
     */
    fun changePublicationStatus(newStatus: PublicationStatus) {
        require(!(_publicationStatus == PublicationStatus.PUBLISHED && newStatus == PublicationStatus.UNPUBLISHED)) {
            "出版済みステータスを未出版に変更することはできません。"
        }
        _publicationStatus = newStatus
    }

    /**
     * 著者を追加する
     */
    fun addAuthor(author: Author) {
        require(!authors.contains(author)) { "この著者は既に追加されています。" }
        (authors as MutableList).add(author)
    }

    /**
     * 著者が含まれているか確認する。
     */
    fun containAuthor(author: Author): Boolean {
        return authors.contains(author)
    }

    /**
     * 著者を削除する
     * 最低1人の著者は残す必要がある
     */
    fun removeAuthor(author: Author) {
        require(authors.size > 1) { "最低1人の著者が必要です。" }
        require(authors.contains(author)) { "この著者は存在しません。" }
        (authors as MutableList).remove(author)
    }
}
