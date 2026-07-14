package com.example.books.infrastructure.driven.postgres

import org.springframework.data.jpa.repository.JpaRepository

interface BookJpaRepository : JpaRepository<BookEntity, Long>
