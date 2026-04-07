package org.goafabric.librarieservice.controller

import jakarta.validation.Valid
import org.goafabric.librarieservice.controller.dto.Book
import org.goafabric.librarieservice.controller.dto.BookSearch
import org.goafabric.librarieservice.logic.BookLogic
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RequestMapping(value = ["/books"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@Validated
class BookController(private val bookLogic: BookLogic) {
        @GetMapping("{id}")
    fun getById(@PathVariable("id") id: String): ResponseEntity<Book> {
        return ResponseEntity.ok(bookLogic.getById(id))
        }

         //ModelAttribute automatically applies RequestParams to the GetMapping, please note that there should be indexes inside the DB for every Attribute
     @GetMapping
    fun find(
           @ModelAttribute bookSearch: BookSearch,
           @RequestParam("page") page: Int = 0,
           @RequestParam("size") size: Int = 20
      ): ResponseEntity<List<Book>> {
        return ResponseEntity.ok(bookLogic.find(bookSearch, maxOf(page, 0), size))
       }

     @GetMapping("isbn/{isbn}")
    fun getByIsbn(@PathVariable("isbn") isbn: String): ResponseEntity<Book> {
        return ResponseEntity.ok(bookLogic.findByIsbn(isbn))
       }

         @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
     fun create(@RequestBody book: @Valid Book): ResponseEntity<Book> {
        val created = bookLogic.save(book)
        return ResponseEntity(status = HttpStatus.CREATED, body = created)
       }

         @PutMapping("{id}")
     fun update(
            @PathVariable("id") id: String,
            @RequestBody book: @Valid Book
      ): ResponseEntity<Book> {
          // Set the ID to the path variable for update
        val bookWithId = book.copy(id = id)
        val updated = bookLogic.save(bookWithId)
        return ResponseEntity.ok(updated)
       }

         @DeleteMapping("{id}")
     fun delete(@PathVariable("id") id: String): ResponseEntity<Unit> {
        bookLogic.delete(id)
        return ResponseEntity(noContent = ok())
       }

     @GetMapping("available")
    fun getAvailableBooks(
           @RequestParam("page") page: Int = 0,
           @RequestParam("size") size: Int = 20
      ): ResponseEntity<List<Book>> {
        return ResponseEntity.ok(bookLogic.getAvailableBooks(maxOf(page, 0), size))
       }

     @GetMapping("search/title-author")
    fun searchByTitleOrAuthor(
           @RequestParam("title", required = false) title: String?,
           @RequestParam("author", required = false) author: String?,
           @RequestParam("page") page: Int = 0,
           @RequestParam("size") size: Int = 20
      ): ResponseEntity<List<Book>> {
        val searchTitle = title ?: ""
        val searchAuthor = author ?: ""
        return ResponseEntity.ok(bookLogic.findByTitleOrAuthor(searchTitle, searchAuthor, maxOf(page, 0), size))
       }

    private fun ok(): Unit = Unit
}
