package org.goafabric.libraryservice.controller

import org.goafabric.libraryservice.controller.dto.Book
import org.goafabric.libraryservice.controller.dto.BookSearch
import org.goafabric.libraryservice.controller.dto.Library
import org.goafabric.libraryservice.controller.dto.LibrarySearch
import org.goafabric.libraryservice.controller.dto.Student
import org.goafabric.libraryservice.logic.LibraryLogic
import org.goafabric.libraryservice.logic.LibraryMapper
import org.goafabric.libraryservice.persistence.BookRepository
import org.goafabric.libraryservice.persistence.StudentRepository
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RequestMapping(value = ["/libraries"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@Validated
class LibraryController(private val logic: LibraryLogic, private val libraryMapping: LibraryMapper) {

    @GetMapping("{id}")
    fun getById(@PathVariable("id") id: String): Library? {
        return logic.getById(id)
    }

    @GetMapping
    fun find(
        @RequestBody librarySearch: LibrarySearch,
        @RequestParam("page") page: Int,
        @RequestParam("size") size: Int
    ): List<Library> {
        return logic.find(librarySearch, page, size)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun save(@RequestBody library: Library): Library {
        return logic.save(library)
    }

    @GetMapping("search")
    fun searchByCity(@RequestParam("city") city: String): List<Library> {
        return logic.findByCity(city, 0, 20)
    }
}

@RequestMapping(value = ["/books"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@Validated
class BookController(private val logic: LibraryLogic, private val bookMapping: BookMapping) {

    @GetMapping("{id}")
    fun getById(@PathVariable("id") id: String): Book? {
        return logic.bookRepository.findById(id).map { bookMapping.map(it) }
    }

    @GetMapping
    fun find(
        @RequestBody bookSearch: BookSearch,
        @RequestParam("page") page: Int,
        @RequestParam("size") size: Int
    ): List<Book> {
        val repository = logic.bookRepository
        return repository.findAll(
            Example.of(bookMapping.map(bookSearch)),
            org.springframework.data.domain.PageRequest.of(page, size)
        ).map { bookMapping.map(it) }.toList()
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun save(@RequestBody book: Book): Book? {
        val repository = logic.bookRepository
        return repository.save(bookMapping.map(book)).map { bookMapping.map(it) }
    }
}

@RequestMapping(value = ["/students"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@Validated
class StudentController(private val logic: LibraryLogic, private val studentMapping: StudentMapping) {

    @GetMapping("{id}")
    fun getById(@PathVariable("id") id: String): Student? {
        return logic.findByStudentNumber(id)
    }

    @GetMapping
    fun find(
        @RequestParam("page") page: Int,
        @RequestParam("size") size: Int
    ): List<Student> {
        val repository = logic.studentRepository
        return repository.findAll(
            org.springframework.data.domain.PageRequest.of(page, size)
        ).map { studentMapping.map(it) }.toList()
    }

    @GetMapping("search")
    fun searchByStudentNumber(@RequestParam("studentNumber") studentNumber: String): Student? {
        return logic.findByStudentNumber(studentNumber)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun save(@RequestBody student: Student): Student? {
        val repository = logic.studentRepository
        return repository.save(studentMapping.map(student)).map { studentMapping.map(it) }
    }
}

@RequestMapping(value = ["/borrow"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class BorrowController(private val logic: LibraryLogic) {

    @GetMapping("/{studentId}/{bookId}")
    fun borrow(@PathVariable("studentId") studentId: String,
                @PathVariable("bookId") bookId: String): org.springframework.http.ResponseEntity<List<String>> {
        return org.springframework.http.ResponseEntity.ok(
            org.springframework.http.ResponseEntity.ok(
                listOf(
                    logic.borrowBook(studentId, bookId).let { "${it.bookId}: ${it.message}" },
                    logic.returnBook(studentId, bookId).let { "${it.bookId}: ${it.message}" }
                ).firstOrNull()
            ))
    }

    @PostMapping("/return/{studentId}/{bookId}")
    fun returnBook(@PathVariable("studentId") studentId: String,
                   @PathVariable("bookId") bookId: String): org.springframework.http.ResponseEntity<Void> {
        try {
            logic.returnBook(studentId, bookId)
            return org.springframework.http.ResponseEntity.ok()
        } catch (e: Exception) {
            return org.springframework.http.responses.ConflictResponse(e.message ?: "Error returned book")
        }
    }

    class BorrowBookResponse(val message: String, val bookId: String)
}

@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(name = ["demo-data:true"])
class DemoController(private val logic: LibraryLogic) {

    @PostMapping("/import")
    fun importDemo(): org.springframework.http.ResponseEntity<Void> {
        return org.springframework.http.ResponseEntity.ok()
    }
}