package com.example.books.infrastructure.application

import com.example.books.domain.port.BookRepository
import com.example.books.domain.usecase.BookUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCasesConfiguration {

    @Bean
    fun bookUseCase(bookRepository: BookRepository): BookUseCase =
        BookUseCase(bookRepository)
}
