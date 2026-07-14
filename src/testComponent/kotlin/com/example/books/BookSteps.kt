package com.example.books

import io.cucumber.java8.En
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus

class BookSteps(
    @Autowired private val restTemplate: TestRestTemplate,
    @Autowired private val bookRepository: InMemoryBookRepository,
) : En {

    @LocalServerPort
    private var port: Int = 0

    private var lastBookId: Long = 0

    init {
        Given("aucun livre n'est enregistré") {
            bookRepository.clear()
        }

        When("j'ajoute le livre {string} de {string}") { title: String, author: String ->
            val response = restTemplate.postForEntity(
                "http://localhost:$port/books",
                mapOf("title" to title, "author" to author),
                String::class.java,
            )
            assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
            lastBookId = Regex("\"id\":(\\d+)").find(response.body.orEmpty())!!.groupValues[1].toLong()
        }

        When("je réserve ce livre") {
            val response = restTemplate.postForEntity(
                "http://localhost:$port/books/$lastBookId/reserve",
                null,
                String::class.java,
            )
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        }

        Then("la liste des livres contient {string} de {string}") { title: String, author: String ->
            val books = bookRepository.findAll()
            assertThat(books).anyMatch { it.title == title && it.author == author }
        }

        Then("la liste des livres est triée par titre") {
            val response = restTemplate.getForEntity("http://localhost:$port/books", String::class.java)
            val titles = Regex("\"title\":\"([^\"]*)\"")
                .findAll(response.body.orEmpty())
                .map { it.groupValues[1] }
                .toList()
            assertThat(titles).isEqualTo(titles.sorted())
        }

        Then("ce livre n'est plus disponible dans la liste") {
            val book = bookRepository.findById(lastBookId)
            assertThat(book?.reserved).isTrue()
        }

        Then("la réservation de ce livre échoue avec le statut {int}") { expectedStatus: Int ->
            val response = restTemplate.postForEntity(
                "http://localhost:$port/books/$lastBookId/reserve",
                null,
                String::class.java,
            )
            assertThat(response.statusCode.value()).isEqualTo(expectedStatus)
        }
    }
}
