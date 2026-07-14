package com.example.books.infrastructure.driving.dto

import com.example.books.domain.model.Book

data class BookDTO(val title: String, val author: String)

fun Book.toDto(): BookDTO = BookDTO(title, author)
