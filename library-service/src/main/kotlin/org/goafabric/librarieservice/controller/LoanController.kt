package org.goafabric.librarieservice.controller

import jakarta.validation.Valid
import org.goafabric.librarieservice.controller.dto.CreateLoanRequest
import org.goafabric.librarieservice.controller.dto.Loan
import org.goafabric.librarieservice.controller.dto.ReturnBookRequest
import org.goafabric.librarieservice.logic.LoanLogic
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RequestMapping(value = ["/loans"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@Validated
class LoanController(private val loanLogic: LoanLogic) {
        @GetMapping("{id}")
    fun getById(@PathVariable("id") id: String): ResponseEntity<Loan> {
        return ResponseEntity.ok(loanLogic.getById(id))
        }

         // Create a new loan (student borrows a book)
     @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createLoan(@RequestBody request: @Valid CreateLoanRequest): ResponseEntity<Loan> {
        val created = loanLogic.createLoan(request)
        return ResponseEntity(status = HttpStatus.CREATED, body = created)
       }

         // Return a borrowed book
     @PostMapping("return")
    fun returnBook(
           @RequestParam("bookId") bookId: String,
           @RequestBody request: @Valid ReturnBookRequest
      ): ResponseEntity<Loan> {
        val returned = loanLogic.returnBook(bookId, request)
        return ResponseEntity.ok(returned)
       }

         // Get all loans for a specific student
     @GetMapping("student/{studentId}")
    fun getLoansForStudent(@PathVariable("studentId") studentId: String): ResponseEntity<List<Loan>> {
        return ResponseEntity.ok(loanLogic.getLoansForStudent(studentId))
       }

         // Get all loan history for a specific book
     @GetMapping("book/{bookId}")
    fun getLoansForBook(@PathVariable("bookId") bookId: String): ResponseEntity<List<Loan>> {
        return ResponseEntity.ok(loanLogic.getLoansForBook(bookId))
       }

         // Get all active loans
     @GetMapping("active")
    fun getActiveLoans(
           @RequestParam("page") page: Int = 0,
           @RequestParam("size") size: Int = 20
      ): ResponseEntity<List<Loan>> {
        return ResponseEntity.ok(loanLogic.getActiveLoans(maxOf(page, 0), size))
       }

         // Get all overdue loans
     @GetMapping("overdue")
    fun getOverdueLoans(
           @RequestParam("page") page: Int = 0,
           @RequestParam("size") size: Int = 20
      ): ResponseEntity<List<Loan>> {
        return ResponseEntity.ok(loanLogic.getOverdueLoans(maxOf(page, 0), size))
       }

         // Get all loans (for admin purposes)
     @GetMapping("")
    fun getAllLoans(
           @RequestParam("page") page: Int = 0,
           @RequestParam("size") size: Int = 20
      ): ResponseEntity<List<Loan>> {
        return ResponseEntity.ok(loanLogic.getLoansForStudent("*").filter { it.id != null }) // Simplified
       }

     companion object {
          private val DEFAULT_PAGE = 0
          private val DEFAULT_SIZE = 20
       }

    private fun maxOf(page: Int, value: Int): Int = kotlin.math.max(page, value)
}
