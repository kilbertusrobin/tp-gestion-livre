package com.example.books.infrastructure.driven.postgres

import com.example.books.domain.model.Book
import com.example.books.domain.port.BookRepository
import org.springframework.stereotype.Repository

@Repository
class PostgresBookRepository(private val bookJpaRepository: BookJpaRepository) : BookRepository {

    override fun save(book: Book): Book = bookJpaRepository.save(BookEntity.fromDomain(book)).toDomain()

    override fun findAll(): List<Book> = bookJpaRepository.findAll().map { it.toDomain() }

    override fun findById(id: Long): Book? = bookJpaRepository.findById(id).map { it.toDomain() }.orElse(null)
}
