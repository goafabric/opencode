package org.goafabric.libraryservice.controller

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.goafabric.libraryservice.controller.dto.*
import org.goafabric.libraryservice.persistence.BookLendingRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class BookLendingControllerIT(
    @Autowired private val bookLendingController: BookLendingController,
    @Autowired private val studentController: StudentController,
    @Autowired private val bookController: BookController,
    @Autowired private val libraryController: LibraryController,
    @Autowired private val bookLendingRepository: BookLendingRepository
) {

    @Test
    fun findByStudent() {
        val students = studentController.find(StudentSearch(firstName = "Lisa"), 0, 1)
        assertThat(students).isNotEmpty()

        val lendings = bookLendingController.findByStudent(students.first().id!!, 0, 10)
        assertThat(lendings).isNotNull().hasSize(2)
    }

    @Test
    fun findActiveByStudent() {
        val students = studentController.find(StudentSearch(firstName = "Homer"), 0, 1)
        assertThat(students).isNotEmpty()

        val lendings = bookLendingController.findActiveByStudent(students.first().id!!, 0, 10)
        assertThat(lendings).isNotNull().hasSize(1)
        assertThat(lendings.first().returnDate).isNull()
    }

    @Test
    fun lendAndReturnBook() {
        // Create a new student and use an existing book to lend
        val student = studentController.save(
            Student(firstName = "Milhouse", lastName = "Van Houten", email = "milhouse@springfield.edu")
        )
        val books = bookController.find(BookSearch(title = "The Pragmatic Programmer"), 0, 1)
        assertThat(books).isNotEmpty()

        // Lend the book
        val lending = bookLendingController.lendBook(student.id!!, books.first().id!!, LocalDate.now().plusDays(14))
        assertThat(lending).isNotNull()
        assertThat(lending.id).isNotNull()
        assertThat(lending.returnDate).isNull()
        assertThat(lending.studentId).isEqualTo(student.id)
        assertThat(lending.bookId).isEqualTo(books.first().id)

        // Return the book
        val returned = bookLendingController.returnBook(lending.id!!)
        assertThat(returned.returnDate).isNotNull()

        // Clean up
        bookLendingRepository.deleteById(lending.id)
        studentController.deleteById(student.id)
    }

    @Test
    fun lendBookAlreadyLent() {
        val students = studentController.find(StudentSearch(firstName = "Homer"), 0, 1)
        assertThat(students).isNotEmpty()

        val lendings = bookLendingController.findActiveByStudent(students.first().id!!, 0, 10)
        assertThat(lendings).isNotEmpty()

        assertThatThrownBy {
            bookLendingController.lendBook(students.first().id!!, lendings.first().bookId, LocalDate.now().plusDays(14))
        }.isInstanceOf(IllegalStateException::class.java)
    }
}
