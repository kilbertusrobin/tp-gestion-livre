package com.example.books

import com.example.books.domain.model.Book
import com.example.books.domain.usecase.BookUseCase
import com.example.books.infrastructure.driving.controller.BookController
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BookController::class)
class BookControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var bookUseCase: BookUseCase

    @Test
    fun `GET slash books retourne la liste des livres`() {
        every { bookUseCase.getAllBooks() } returns listOf(
            Book("Clean Code", "Robert C. Martin"),
            Book("Refactoring", "Martin Fowler"),
        )

        mockMvc.perform(get("/books"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].title").value("Clean Code"))
            .andExpect(jsonPath("$[1].title").value("Refactoring"))

        verify(exactly = 1) { bookUseCase.getAllBooks() }
    }

    @Test
    fun `POST slash books cree un livre`() {
        every { bookUseCase.addBook("Clean Code", "Robert C. Martin") } returns
            Book("Clean Code", "Robert C. Martin")

        mockMvc.perform(
            post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"title":"Clean Code","author":"Robert C. Martin"}"""),
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.title").value("Clean Code"))
            .andExpect(jsonPath("$.author").value("Robert C. Martin"))

        verify(exactly = 1) { bookUseCase.addBook("Clean Code", "Robert C. Martin") }
    }

    @Test
    fun `POST slash books retourne 400 si le titre est vide`() {
        every { bookUseCase.addBook("", "Robert C. Martin") } throws
            IllegalArgumentException("Le titre ne peut pas être vide")

        mockMvc.perform(
            post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"title":"","author":"Robert C. Martin"}"""),
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().string("Le titre ne peut pas être vide"))
    }
}
