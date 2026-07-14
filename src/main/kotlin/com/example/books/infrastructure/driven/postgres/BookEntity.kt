package com.example.books.infrastructure.driven.postgres

import com.example.books.domain.model.Book
import jakarta.persistence.Column
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
    @Column(nullable = false)
    val reserved: Boolean = false,
) {
    fun toDomain(): Book = Book(id = id, title = title, author = author, reserved = reserved)

    companion object {
        fun fromDomain(book: Book): BookEntity =
            BookEntity(id = book.id, title = book.title, author = book.author, reserved = book.reserved)
    }
}
