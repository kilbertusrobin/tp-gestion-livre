package com.example.books.infrastructure.driven.postgres

import com.example.books.domain.model.Book
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "book")
class BookEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val title: String,
    val author: String,
) {
    fun toDomain(): Book = Book(title, author)

    companion object {
        fun fromDomain(book: Book): BookEntity = BookEntity(title = book.title, author = book.author)
    }
}
