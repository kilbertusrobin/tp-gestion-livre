package com.example.books.domain.usecase

import com.example.books.domain.model.Book
import com.example.books.domain.port.BookRepository

class BookUseCase(private val repository: BookRepository) {

    fun addBook(title: String, author: String): Book {
        require(title.isNotBlank()) { "Le titre ne peut pas être vide" }
        require(author.isNotBlank()) { "L'auteur ne peut pas être vide" }
        val book = Book(title.trim(), author.trim())
        repository.save(book)
        return book
    }

    fun getAllBooks(): List<Book> = repository.findAll().sortedBy { it.title }
}
