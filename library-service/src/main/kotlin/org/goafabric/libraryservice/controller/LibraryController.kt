package org.goafabric.libraryservice.controller

import org.springframework.web.bind.annotation.*
import org.springframework.http.*
import org.goafabric.libraryservice.logic.LibraryService
import org.goafabric.libraryservice.persistence.entity.BookEo
import org.goafabric.libraryservice.persistence.entity.StudentEo
import java.util.*

@RestController
@RequestMapping("/api/library")
class LibraryController(
    private val libraryService: LibraryService
) {

    @PostMapping("/lend")
    fun lendBook(@RequestParam studentId: Long, @RequestParam bookId: Long): ResponseEntity<Map<String, Any>> {
        try {
            libraryService.lendBook(studentId, bookId)
            return ResponseEntity.ok(mapOf("message" to "Book lent successfully"))
        } catch (e: ResourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        } catch (e: IllegalStateException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to e.message))
        }
    }

    @PutMapping("/return")
    fun returnBook(@RequestParam bookId: Long): ResponseEntity<Map<String, Any>> {
        try {
            libraryService.returnBook(bookId)
            return ResponseEntity.ok(mapOf("message" to "Book returned successfully"))
        } catch (e: ResourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        } catch (e: IllegalStateException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/books")
    fun listBooks(): ResponseEntity<List<BookEo>> {
        // In a real implementation, this would fetch from the repository
        // For demonstration, returning an empty list
        return ResponseEntity.ok(emptyList())
    }
}