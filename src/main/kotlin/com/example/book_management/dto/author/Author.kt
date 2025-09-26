package com.example.book_management.dto.author

import com.example.book_management.dto.book.Book

/**
 * 著者エンティティ
 */
class Author(
    val id: AuthorId,
    val name: AuthorName,
    val birthDate: BirthDate
) {
    private val _books: MutableList<Book> = mutableListOf()
    val books: List<Book> get() = _books.toList()

    /**
     * 書籍を追加する
     */
    fun addBook(book: Book) {
        require(book.containAuthor(this)) { "この著者が執筆した書籍のみ追加できます。" }
        _books.add(book)
    }
}
