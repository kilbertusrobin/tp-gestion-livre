package com.example.books.domain.port

import com.example.books.domain.model.Book

interface BookRepository {
    fun save(book: Book)
    fun findAll(): List<Book>
}
