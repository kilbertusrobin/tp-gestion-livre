package com.example.books.infrastructure.driving.controller

import com.example.books.domain.usecase.BookUseCase
import com.example.books.infrastructure.driving.dto.BookDTO
import com.example.books.infrastructure.driving.dto.toDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController(private val bookUseCase: BookUseCase) {

    @GetMapping
    fun getAllBooks(): List<BookDTO> = bookUseCase.getAllBooks().map { it.toDto() }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addBook(@RequestBody bookDTO: BookDTO): BookDTO =
        bookUseCase.addBook(bookDTO.title, bookDTO.author).toDto()

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleInvalidBook(exception: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity.badRequest().body(exception.message)
}
