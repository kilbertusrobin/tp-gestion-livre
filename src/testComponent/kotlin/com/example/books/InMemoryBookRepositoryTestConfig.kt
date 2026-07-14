package com.example.books

import com.example.books.domain.model.Book
import com.example.books.domain.port.BookRepository
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

class InMemoryBookRepository : BookRepository {
    private val books = mutableListOf<Book>()

    override fun save(book: Book) {
        books.add(book)
    }

    override fun findAll(): List<Book> = books.toList()

    fun clear() {
        books.clear()
    }
}

@TestConfiguration
class InMemoryBookRepositoryTestConfig {

    @Bean
    @Primary
    fun bookRepository(): InMemoryBookRepository = InMemoryBookRepository()
}
