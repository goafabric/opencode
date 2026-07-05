package org.goafabric.libraryservice.logic

import io.quarkus.test.junit.QuarkusTest
import org.goafabric.libraryservice.persistence.BookRepository
import org.goafabric.libraryservice.persistence.StudentRepository
import org.goafabric.libraryservice.persistence.entity.BookEo
import org.goafabric.libraryservice.persistence.entity.StudentEo
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryManaged
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

/**
 * Unit tests for LibraryLogic.
 */
@QuarkusTest
class LibraryLogicTest {
    
    private val studentRepo = mockk<StudentRepository>()
    private val bookRepo = mockk<BookRepository>()
    private val logic = LibraryLogic(studentRepo, bookRepo)

     @Test
    fun `lendBooks should reduce available copies`() {
         // Arrange
        every { studentRepo.findById("studentId") } returns StudentEo(name = "TestStudent")
        val book = BookEo(availableCopies = 1)
        every { bookRepo.findById("bookId") } returns book
        
         // Act
         logic.lendBooks("studentId", listOf("bookId"))
        
         // Assert
         assertThat(book.availableCopies).isEqualTo(0)
    }

      @Test
    fun `lendBooks should throw if student is not found`() {
         every { studentRepo.findById(any()) } returns null
         
         // Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException::class.java) {
            logic.lendBooks("nonExistent", listOf("bookId"))
        }
    }
}
