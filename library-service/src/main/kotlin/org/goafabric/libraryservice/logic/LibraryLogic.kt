package org.goafabric.libraryservice.logic

import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.goafabric.libraryservice.persistence.BookRepository
import org.goafabric.libraryservice.persistence.StudentRepository
import org.goafabric.libraryservice.persistence.entity.BookEo
import org.goafabric.libraryservice.persistence.entity.StudentEo

/**
 * Service containing business logic for the library.
 */
@ApplicationScoped
class LibraryLogic(
    private val studentRepository: StudentRepository,
    private val bookRepository: BookRepository
) {

    /**
     * Marks books as lent to a specific student.
     */
    @Transactional
    fun lendBooks(studentId: String, bookIds: List<String>) {
        require(studentId.isNotBlank()) { "Student ID cannot be blank" }
        require(bookIds.isNotEmpty()) { "Must lend at least one book" }

        val student = studentRepository.findById(studentId) ?: throw IllegalArgumentException("Student not found")
        
        for (bookId in bookIds) {
            val book = bookRepository.findById(bookId) ?: continue
            if (book.availableCopies > 0) {
                book.availableCopies--
            }
        }
    }
}
