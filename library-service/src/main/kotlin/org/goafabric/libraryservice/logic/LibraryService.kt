package org.goafabric.libraryservice.logic

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.goafabric.libraryservice.persistence.StudentRepository
import org.goafabric.libraryservice.persistence.BookRepository
import org.goafabric.libraryservice.persistence.entity.StudentEo
import org.goafabric.libraryservice.persistence.entity.BookEo
import org.goafabric.libraryservice.persistence.entity.BookStatus
import java.util.Optional

@Service
class LibraryService(
    private val studentRepository: StudentRepository,
    private val bookRepository: BookRepository
) {

    @Transactional
    fun lendBook(studentId: Long, bookId: Long) {
        val studentOpt = studentRepository.findById(studentId)
        val bookOpt = bookRepository.findById(bookId)

        if (studentOpt.isEmpty()) throw ResourceNotFoundException("Student not found")
        if (bookOpt.isEmpty()) throw ResourceNotFoundException("Book not found")
        val student = studentOpt.get()
        val book = bookOpt.get()

        if (book.status != BookStatus.AVAILABLE) {
            throw IllegalStateException("Book is not available for lending")
        }

        // Add book to student's collection
        student.books.add(book)
        // Mark book as borrowed
        book.borrow(student)

        studentRepository.save(student)
        bookRepository.save(book)
    }

    @Transactional
    fun returnBook(bookId: Long) {
        val bookOpt = bookRepository.findById(bookId)
        if (bookOpt.isEmpty()) throw ResourceNotFoundException("Book not found")
        val book = bookOpt.get()

        if (book.status != BookStatus.BORROWED) {
            throw IllegalStateException("Book is not currently borrowed")
        }

        // Return book
        book.returnBook()
        bookRepository.save(book)
    }
}

class ResourceNotFoundException(message: String) : RuntimeException(message)