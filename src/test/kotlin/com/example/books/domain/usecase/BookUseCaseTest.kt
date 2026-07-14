package com.example.books.domain.usecase

import com.example.books.domain.model.Book
import com.example.books.domain.port.BookRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify

class BookUseCaseTest : DescribeSpec({

    describe("BookUseCase") {

        // ── shared fixtures ────────────────────────────────────────────────
        val repository = mockk<BookRepository>(relaxed = true)
        val useCase = BookUseCase(repository)

        // Reset mocks between tests via beforeEach
        beforeEach { io.mockk.clearMocks(repository) }

        // ── addBook ────────────────────────────────────────────────────────
        describe("addBook") {

            it("crée et sauvegarde un livre avec titre et auteur valides") {
                val slot = slot<Book>()
                every { repository.save(capture(slot)) } answers { firstArg<Book>().copy(id = 1L) }

                val book = useCase.addBook("Clean Code", "Robert C. Martin")

                book shouldBe Book(id = 1L, title = "Clean Code", author = "Robert C. Martin")
                slot.captured shouldBe Book(title = "Clean Code", author = "Robert C. Martin")
                verify(exactly = 1) { repository.save(any()) }
            }

            it("lève une exception si le titre est vide") {
                shouldThrow<IllegalArgumentException> {
                    useCase.addBook("", "Robert C. Martin")
                }
                verify(exactly = 0) { repository.save(any()) }
            }

            it("lève une exception si le titre ne contient que des espaces") {
                shouldThrow<IllegalArgumentException> {
                    useCase.addBook("   ", "Robert C. Martin")
                }
            }

            it("lève une exception si l'auteur est vide") {
                shouldThrow<IllegalArgumentException> {
                    useCase.addBook("Clean Code", "")
                }
                verify(exactly = 0) { repository.save(any()) }
            }

            it("lève une exception si l'auteur ne contient que des espaces") {
                shouldThrow<IllegalArgumentException> {
                    useCase.addBook("Clean Code", "   ")
                }
            }

            it("retire les espaces de bord du titre et de l'auteur") {
                val slot = slot<Book>()
                every { repository.save(capture(slot)) } answers { firstArg<Book>().copy(id = 1L) }

                useCase.addBook("  Clean Code  ", "  Robert C. Martin  ")

                slot.captured shouldBe Book(title = "Clean Code", author = "Robert C. Martin")
            }
        }

        // ── getAllBooks ────────────────────────────────────────────────────
        describe("getAllBooks") {

            it("retourne une liste vide quand aucun livre n'est stocké") {
                every { repository.findAll() } returns emptyList()

                useCase.getAllBooks().shouldBeEmpty()
            }

            it("retourne un seul livre") {
                val book = Book(id = 1L, title = "Refactoring", author = "Martin Fowler")
                every { repository.findAll() } returns listOf(book)

                useCase.getAllBooks() shouldContainExactly listOf(book)
            }

            it("retourne la liste triée par titre (ordre alphabétique)") {
                val books = listOf(
                    Book(id = 1L, title = "Refactoring", author = "Martin Fowler"),
                    Book(id = 2L, title = "Clean Code", author = "Robert C. Martin"),
                    Book(id = 3L, title = "The Pragmatic Programmer", author = "Andrew Hunt"),
                )
                every { repository.findAll() } returns books

                useCase.getAllBooks() shouldContainExactly listOf(
                    Book(id = 2L, title = "Clean Code", author = "Robert C. Martin"),
                    Book(id = 1L, title = "Refactoring", author = "Martin Fowler"),
                    Book(id = 3L, title = "The Pragmatic Programmer", author = "Andrew Hunt"),
                )
            }

            it("retourne la liste triée quand les livres sont déjà stockés dans l'ordre") {
                val books = listOf(
                    Book(id = 1L, title = "A", author = "Author 1"),
                    Book(id = 2L, title = "B", author = "Author 2"),
                    Book(id = 3L, title = "C", author = "Author 3"),
                )
                every { repository.findAll() } returns books

                useCase.getAllBooks() shouldContainExactly books
            }
        }

        // ── reserveBook ────────────────────────────────────────────────────
        describe("reserveBook") {

            it("réserve un livre disponible") {
                val book = Book(id = 1L, title = "Clean Code", author = "Robert C. Martin", reserved = false)
                every { repository.findById(1L) } returns book
                every { repository.save(any()) } answers { firstArg() }

                val reserved = useCase.reserveBook(1L)

                reserved.reserved shouldBe true
                verify(exactly = 1) { repository.save(book.copy(reserved = true)) }
            }

            it("lève une exception si le livre est déjà réservé") {
                val book = Book(id = 1L, title = "Clean Code", author = "Robert C. Martin", reserved = true)
                every { repository.findById(1L) } returns book

                shouldThrow<IllegalStateException> {
                    useCase.reserveBook(1L)
                }
                verify(exactly = 0) { repository.save(any()) }
            }

            it("lève une exception si le livre n'existe pas") {
                every { repository.findById(42L) } returns null

                shouldThrow<NoSuchElementException> {
                    useCase.reserveBook(42L)
                }
                verify(exactly = 0) { repository.save(any()) }
            }
        }

        // ── tests de propriétés ────────────────────────────────────────────
        describe("propriétés") {

            val nonBlankString = Arb.string(1..50).filter { it.isNotBlank() }

            it("la liste retournée contient exactement tous les éléments stockés") {
                checkAll(Arb.list(nonBlankString, 0..20)) { titles ->
                    val stored = titles.map { Book(title = it, author = "Auteur") }
                    every { repository.findAll() } returns stored

                    val result = useCase.getAllBooks()

                    result shouldHaveSize stored.size
                    result shouldContainExactlyInAnyOrder stored
                }
            }

            it("la liste retournée est triée par titre") {
                checkAll(Arb.list(nonBlankString, 1..20)) { titles ->
                    val stored = titles.map { Book(title = it, author = "Auteur") }
                    every { repository.findAll() } returns stored

                    val result = useCase.getAllBooks()

                    result shouldBe result.sortedBy { it.title }
                }
            }

            it("addBook avec titre et auteur non vides sauvegarde toujours le livre") {
                checkAll(nonBlankString, nonBlankString) { title, author ->
                    io.mockk.clearMocks(repository)
                    every { repository.save(any()) } answers { firstArg() }

                    useCase.addBook(title, author)

                    verify(exactly = 1) { repository.save(any()) }
                }
            }

            it("addBook avec titre vide lève toujours IllegalArgumentException") {
                val blankString = Arb.string(0..20).filter { it.isBlank() }
                checkAll(blankString, nonBlankString) { blank, author ->
                    shouldThrow<IllegalArgumentException> {
                        useCase.addBook(blank, author)
                    }
                }
            }
        }
    }
})
