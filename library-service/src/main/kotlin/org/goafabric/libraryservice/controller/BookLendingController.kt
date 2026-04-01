package org.goafabric.libraryservice.controller

import org.goafabric.libraryservice.controller.dto.BookLending
import org.goafabric.libraryservice.logic.BookLendingLogic
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RequestMapping(value = ["/lendings"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class BookLendingController(private val bookLendingLogic: BookLendingLogic) {
    @GetMapping("{id}")
    fun getById(@PathVariable("id") id: String): BookLending {
        return bookLendingLogic.getById(id)
    }

    @GetMapping("student/{studentId}")
    fun findByStudent(
        @PathVariable("studentId") studentId: String,
        @RequestParam("page") page: Int, @RequestParam("size") size: Int
    ): List<BookLending> {
        return bookLendingLogic.findByStudent(studentId, page, size)
    }

    @GetMapping("student/{studentId}/active")
    fun findActiveByStudent(
        @PathVariable("studentId") studentId: String,
        @RequestParam("page") page: Int, @RequestParam("size") size: Int
    ): List<BookLending> {
        return bookLendingLogic.findActiveByStudent(studentId, page, size)
    }

    @GetMapping("book/{bookId}")
    fun findByBook(
        @PathVariable("bookId") bookId: String,
        @RequestParam("page") page: Int, @RequestParam("size") size: Int
    ): List<BookLending> {
        return bookLendingLogic.findByBook(bookId, page, size)
    }

    @PostMapping("lend")
    fun lendBook(
        @RequestParam("studentId") studentId: String,
        @RequestParam("bookId") bookId: String,
        @RequestParam("dueDate") dueDate: LocalDate
    ): BookLending {
        return bookLendingLogic.lendBook(studentId, bookId, dueDate)
    }

    @PostMapping("{id}/return")
    fun returnBook(@PathVariable("id") id: String): BookLending {
        return bookLendingLogic.returnBook(id)
    }
}
