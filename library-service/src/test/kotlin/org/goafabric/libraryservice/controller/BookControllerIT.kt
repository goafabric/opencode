package org.goafabric.libraryservice.controller

import org.assertj.core.api.Assertions.assertThat
import org.goafabric.libraryservice.controller.dto.*
import org.goafabric.libraryservice.persistence.BookLendingRepository
import org.goafabric.libraryservice.persistence.BookRepository
import org.goafabric.libraryservice.persistence.StudentRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class BookControllerIT(
    @Autowired private val bookController: BookController,
    @Autowired private val libraryController: LibraryController,
    @Autowired private val bookRepository: BookRepository
) {

    @Test
    fun findAll() {
        val books = bookController.find(BookSearch(), 0, 20)
        assertThat(books).isNotNull().hasSize(5)
    }

    @Test
    fun findById() {
        val books = bookController.find(BookSearch(), 0, 20)
        assertThat(books).isNotEmpty()

        val book = bookController.getById(books.first().id!!)
        assertThat(book).isNotNull()
        assertThat(book.title).isEqualTo(books.first().title)
    }

    @Test
    fun findByTitle() {
        val books = bookController.find(BookSearch(title = "Clean Code"), 0, 10)
        assertThat(books).isNotNull().hasSize(1)
        assertThat(books.first().title).isEqualTo("Clean Code")
        assertThat(books.first().author).isEqualTo("Robert C. Martin")
    }

    @Test
    fun findByLibrary() {
        val libraries = libraryController.findByName("Central", 0, 1)
        assertThat(libraries).isNotEmpty()

        val books = bookController.findByLibrary(libraries.first().id!!, 0, 10)
        assertThat(books).isNotNull().hasSize(3)
    }

    @Test
    fun findByIsbn() {
        val book = bookController.findByIsbn("9780132350884")
        assertThat(book).isNotNull()
        assertThat(book!!.title).isEqualTo("Clean Code")
    }

    @Test
    fun save() {
        val libraries = libraryController.findAll(0, 1)
        val book = bookController.save(
            Book(title = "Test Book", author = "Test Author", isbn = "1234567890", libraryId = libraries.first().id!!)
        )

        assertThat(book).isNotNull()
        assertThat(book.id).isNotNull()
        assertThat(book.version).isEqualTo(0)

        bookRepository.deleteById(book.id!!)
    }
}
