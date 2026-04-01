package org.goafabric.libraryservice.controller

import jakarta.validation.Valid
import org.goafabric.libraryservice.controller.dto.Book
import org.goafabric.libraryservice.controller.dto.BookSearch
import org.goafabric.libraryservice.logic.BookLogic
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RequestMapping(value = ["/books"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@Validated
class BookController(private val bookLogic: BookLogic) {
    @GetMapping("{id}")
    fun getById(@PathVariable("id") id: String): Book {
        return bookLogic.getById(id)
    }

    @GetMapping
    fun find(
        @ModelAttribute bookSearch: BookSearch,
        @RequestParam("page") page: Int, @RequestParam("size") size: Int
    ): List<Book> {
        return bookLogic.find(bookSearch, page, size)
    }

    @GetMapping("library/{libraryId}")
    fun findByLibrary(
        @PathVariable("libraryId") libraryId: String,
        @RequestParam("page") page: Int, @RequestParam("size") size: Int
    ): List<Book> {
        return bookLogic.findByLibrary(libraryId, page, size)
    }

    @GetMapping("isbn")
    fun findByIsbn(@RequestParam("isbn") isbn: String): Book? {
        return bookLogic.findByIsbn(isbn)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun save(@RequestBody book: @Valid Book): Book {
        return bookLogic.save(book)
    }

    @DeleteMapping("{id}")
    fun deleteById(@PathVariable("id") id: String) {
        bookLogic.deleteById(id)
    }
}
