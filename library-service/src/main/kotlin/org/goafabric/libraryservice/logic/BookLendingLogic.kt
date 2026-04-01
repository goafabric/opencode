package org.goafabric.libraryservice.logic

import org.goafabric.libraryservice.controller.dto.BookLending
import org.goafabric.libraryservice.persistence.BookLendingRepository
import org.goafabric.libraryservice.persistence.BookRepository
import org.goafabric.libraryservice.persistence.StudentRepository
import org.goafabric.libraryservice.persistence.entity.BookLendingEo
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
@Transactional
class BookLendingLogic(
    private val bookLendingMapper: BookLendingMapper,
    private val bookLendingRepository: BookLendingRepository,
    private val studentRepository: StudentRepository,
    private val bookRepository: BookRepository
) {
    fun getById(id: String): BookLending {
        return bookLendingMapper.map(
            bookLendingRepository.findById(id).orElseThrow()
        )
    }

    fun findByStudent(studentId: String, page: Int, size: Int): List<BookLending> {
        return bookLendingMapper.map(
            bookLendingRepository.findByStudentId(studentId, PageRequest.of(page, size))
        )
    }

    fun findByBook(bookId: String, page: Int, size: Int): List<BookLending> {
        return bookLendingMapper.map(
            bookLendingRepository.findByBookId(bookId, PageRequest.of(page, size))
        )
    }

    fun findActiveByStudent(studentId: String, page: Int, size: Int): List<BookLending> {
        return bookLendingMapper.map(
            bookLendingRepository.findByStudentIdAndReturnDateIsNull(studentId, PageRequest.of(page, size))
        )
    }

    fun lendBook(studentId: String, bookId: String, dueDate: LocalDate): BookLending {
        val student = studentRepository.findById(studentId).orElseThrow {
            IllegalArgumentException("Student with id $studentId not found")
        }
        val book = bookRepository.findById(bookId).orElseThrow {
            IllegalArgumentException("Book with id $bookId not found")
        }

        val existingLending = bookLendingRepository.findByStudentIdAndBookIdAndReturnDateIsNull(studentId, bookId)
        if (existingLending != null) {
            throw IllegalStateException("Book is already lent to this student")
        }

        val lending = BookLendingEo(
            id = null,
            organizationId = null,
            student = student,
            book = book,
            borrowDate = LocalDate.now(),
            dueDate = dueDate,
            returnDate = null,
            version = null
        )
        return bookLendingMapper.map(bookLendingRepository.save(lending))
    }

    fun returnBook(lendingId: String): BookLending {
        val lending = bookLendingRepository.findById(lendingId).orElseThrow {
            IllegalArgumentException("Lending with id $lendingId not found")
        }
        if (lending.returnDate != null) {
            throw IllegalStateException("Book has already been returned")
        }
        lending.returnDate = LocalDate.now()
        return bookLendingMapper.map(bookLendingRepository.save(lending))
    }
}
