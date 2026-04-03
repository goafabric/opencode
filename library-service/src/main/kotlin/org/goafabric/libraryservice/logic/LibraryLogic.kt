package org.goafabric.libraryservice.logic

import org.goafabric.libraryservice.adapter.BorrowBookResponse
import org.goafabric.libraryservice.controller.dto.Book
import org.goafabric.libraryservice.controller.dto.Library
import org.goafabric.libraryservice.controller.dto.Student
import org.goafabric.libraryservice.controller.dto.StudentSearch
import org.goafabric.libraryservice.controller.dto.StudentSearchFilter
import org.goafabric.libraryservice.persistence.BookRepository
import org.goafabric.libraryservice.persistence.LibraryRepository
import org.goafabric.libraryservice.persistence.StudentRepository
import org.goafabric.libraryservice.persistence.entity.BookEo
import org.goafabric.libraryservice.persistence.entity.StudentEo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class LibraryLogic(
    private val libraryRepository: LibraryRepository,
    private val bookRepository: BookRepository,
    private val studentRepository: StudentRepository,
    private val libraryMapper: LibraryMapper,
    private val bookMapper: BookMapper,
    private val studentMapper: StudentMapper
) {

    @Transactional(readOnly = true)
    fun getById(id: String): Library? {
        return libraryMapper.map(
            libraryRepository.findById(id)
                .orElse(null)
        )
    }

    @Transactional(readOnly = true)
    fun find(
        librarySearch: LibrarySearch,
        page: Int,
        size: Int
    ): List<Library> {
        return libraryMapper.map(
            libraryRepository.findAll(
                example = exampleQuery(librarySearch),
                page = org.springframework.data.domain.PageRequest.of(page, size)
            )
        )
    }

    @Transactional(readOnly = true)
    fun findByCity(city: String, page: Int, size: Int): List<Library> {
        return libraryMapper.map(
            libraryRepository.findByCity(city, org.springframework.data.domain.PageRequest.of(page, size))
        )
    }

    @Transactional
    fun save(library: Library): Library {
        return libraryMapper.map(
            libraryRepository.save(
                libraryMapper.map(library)
            )
        )
    }

    @Transactional
    fun borrowBook(studentId: String, bookId: String): BorrowBookResponse {
        return try {
            val student = studentRepository.findById(studentId).orElseThrow {
                IllegalStateException("Student not found: $studentId")
            }
            val book = bookRepository.findById(bookId).orElseThrow {
                IllegalStateException("Book not found: $bookId")
            }

            // Check if student already has the book
            if (student.borrowingBooks.contains(bookId)) {
                throw IllegalStateException("Student already has this book: $bookId")
            }

            // Add book to student's borrowing list
            student.borrowingBooks.add(bookId)
            studentRepository.save(student)
            BorrowBookResponse(bookId, "Borrowed successfully")
        } catch (e: IllegalArgumentException) {
            throw e
        } catch (e: IllegalStateException) {
            throw e
        }
    }

    @Transactional
    fun returnBook(studentId: String, bookId: String): BorrowBookResponse {
        return try {
            val student = studentRepository.findById(studentId).orElseThrow {
                IllegalStateException("Student not found: $studentId")
            }

            // Remove book from student's borrowing list
            student.borrowingBooks.remove(bookId)
            studentRepository.save(student)
            BorrowBookResponse(bookId, "Returned successfully")
        } catch (e: IllegalArgumentException) {
            throw e
        } catch (e: IllegalStateException) {
            throw e
        }
    }

    @Transactional(readOnly = true)
    fun findByStudentNumber(studentNumber: String): Student? {
        return libraryMapper.map(
            studentRepository.findByStudentNumber(studentNumber)
        )
    }

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable, filter: StudentSearchFilter?): Page<Student> {
        return when (filter) {
            null -> libraryMapper.map(
                libraryRepository.findAll(pageable)
            )
            else -> when {
                filter.firstName.isNotBlank() && filter.lastName.isNotBlank() -> libraryMapper.map(
                    libraryRepository.findByFirstNameAndLastNameAndPageable(
                        filter.firstName, filter.lastName, pageable
                    )
                )
                else -> libraryMapper.map(
                    libraryRepository.findAll(pageable)
                )
            }
        }
    }

    private fun exampleQuery(librarySearch: LibrarySearch): org.springframework.data.domain.Criteria {
        val example = libraryMapper.map(librarySearch).toCriteria()
        return example
    }
}

@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(name = ["enable-demo-data"])
class DemoDataImporter(
    private val libraryRepository: LibraryRepository,
    private val bookRepository: BookRepository,
    private val studentRepository: StudentRepository
) {

    @Transactional
    fun importDemo() {
        // Create demo libraries
        val library1 = libraryRepository.save(
            libraryRepository.insert(
                "Downtown Public Library",
                "Located in downtown area with extensive collections",
                "123 Main Street",
                "NY",
                "10001"
            )
        )
        val library2 = libraryRepository.save(
            libraryRepository.insert(
                "Central Library",
                "Central branch with many resources",
                "456 Central Ave",
                "CA",
                "90001"
            )
        )

        // Create demo books
        val book1 = bookRepository.save(
            bookRepository.insert(
                "The Great Gatsby",
                "F. Scott Fitzgerald",
                "978-0743273565",
                180,
                1925
            )
        )
        val book2 = bookRepository.save(
            bookRepository.insert(
                "1984",
                "George Orwell",
                "978-0451524935",
                256,
                1949
            )
        )
        val book3 = bookRepository.save(
            bookRepository.insert(
                "To Kill a Mockingbird",
                "Harper Lee",
                "978-0061120084",
                324,
                1960
            )
        )

        // Create demo students
        val student1 = studentRepository.save(
            studentRepository.insert(
                "John",
                "Doe",
                "john.doe@example.com",
                "555-0100",
                "STU001",
                "2024",
                "Computer Science",
                AddressEo("100 Campus Drive", "NY", "10001")
            )
        )
        val student2 = studentRepository.save(
            studentRepository.insert(
                "Jane",
                "Smith",
                "jane.smith@example.com",
                "555-0101",
                "STU002",
                "2024",
                "Engineering",
                AddressEo("200 Campus Drive", "CA", "90001")
            )
        )

        println("Demo data imported successfully!")
        println("Libraries created: ${library1.name}, ${library2.name}")
        println("Books created: ${book1.title}, ${book2.title}, ${book3.title}")
        println("Students created: ${student1.firstName} ${student1.lastName}, ${student2.firstName} ${student2.lastName}")
    }
}