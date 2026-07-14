package com.example.books.domain.usecase

import com.example.books.domain.model.Book
import com.example.books.domain.port.BookRepository

class BookUseCase(private val repository: BookRepository) {

    fun addBook(title: String, author: String): Book {
        require(title.isNotBlank()) { "Le titre ne peut pas être vide" }
        require(author.isNotBlank()) { "L'auteur ne peut pas être vide" }
        val book = Book(title = title.trim(), author = author.trim())
        return repository.save(book)
    }

    fun getAllBooks(): List<Book> = repository.findAll().sortedBy { it.title }

    fun reserveBook(id: Long): Book {
        val book = repository.findById(id) ?: throw NoSuchElementException("Aucun livre trouvé avec l'id $id")
        check(!book.reserved) { "Le livre est déjà réservé" }
        return repository.save(book.copy(reserved = true))
    }
}
