package com.example.books.domain.model

data class Book(
    val id: Long? = null,
    val title: String,
    val author: String,
    val reserved: Boolean = false,
)
