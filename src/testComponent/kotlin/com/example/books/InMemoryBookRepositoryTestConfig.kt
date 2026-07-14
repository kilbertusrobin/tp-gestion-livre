package com.example.books

import com.example.books.domain.model.Book
import com.example.books.domain.port.BookRepository
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

class InMemoryBookRepository : BookRepository {
    private val books = mutableListOf<Book>()
    private var nextId = 1L

    override fun save(book: Book): Book {
        val saved = if (book.id == null) book.copy(id = nextId++) else book
        books.removeAll { it.id == saved.id }
        books.add(saved)
        return saved
    }

    override fun findAll(): List<Book> = books.toList()

    override fun findById(id: Long): Book? = books.find { it.id == id }

    fun clear() {
        books.clear()
        nextId = 1L
    }
}

@TestConfiguration
class InMemoryBookRepositoryTestConfig {

    @Bean
    @Primary
    fun bookRepository(): InMemoryBookRepository = InMemoryBookRepository()
}
