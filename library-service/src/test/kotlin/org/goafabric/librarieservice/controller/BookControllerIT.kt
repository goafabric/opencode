package org.goafabric.librarieservice.controller

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.goafabric.librarieservice.controller.dto.Book
import org.goafabric.librarieservice.controller.dto.BookSearch
import org.goafabric.librarieservice.logic.BookLogic
import org.junit.jupiter.api.Test

@QuarkusTest
class BookControllerIT {

       @Inject
    lateinit var bookController: BookController

       @Inject
    lateinit var bookLogic: BookLogic

       @Test
    fun findById() {
        val books: List<Book> = bookController.find(BookSearch(null, null, null, null), 0, 10)
        assertThat(books).isNotNull().hasSize(2)

        val book = bookController.getById(books.first().id!!)
        assertThat(book).isNotNull()
        assertThat(book.title).isEqualTo(books.first().title)
        assertThat(book.author).isEqualTo(books.first().author)
           }

       @Test
    fun findAll() {
        assertThat(bookController.find(BookSearch(null, null, null, null), 0, 10)).isNotNull().hasSize(2)
           }

       @Test
    fun findByTitle() {
        val books: List<Book> = bookController.find(BookSearch("The Simpsons", null, null, null), 0, 10)
        assertThat(books).isNotNull().hasSize(1)
        assertThat(books.first().title).isEqualTo("The Simpsons")
        assertThat(books.first().author).isEqualTo("Matt Groening")
           }

       @Test
    fun saveLendReturnDelete() {
        val book = bookController.save(
            Book(
                null, null,
                  "978-0999999999",
                  "A Self Made Book",
                  "Anonymous",
                  null
             )
          )

        assertThat(book).isNotNull()
        assertThat(book.version).isEqualTo(0)

          //lend a book to a student, a book can be held by at most one student
        val lender = "student-fixed-id"
        bookController.lend(book.id!!, lender)
        val lent = bookController.getById(book.id!!)
        assertThat(lent.studentId).isEqualTo(lender)

          //return the previously lent book, frees it for the next student
        bookController.returnBook(book.id!!)
        val returned = bookController.getById(book.id!!)
        assertThat(returned.studentId).isNullOrEmpty()

        bookLogic.delete(book.id)
           }
}
