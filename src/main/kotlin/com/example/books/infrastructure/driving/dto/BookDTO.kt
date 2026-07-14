package com.example.books.infrastructure.driving.dto

import com.example.books.domain.model.Book

data class BookDTO(
    val id: Long? = null,
    val title: String,
    val author: String,
    val available: Boolean = true,
)

fun Book.toDto(): BookDTO = BookDTO(id = id, title = title, author = author, available = !reserved)
