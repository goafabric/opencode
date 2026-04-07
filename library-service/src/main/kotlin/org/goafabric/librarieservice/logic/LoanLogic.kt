package org.goafabric.librarieservice.logic

import org.goafabric.librarieservice.controller.dto.CreateLoanRequest
import org.goafabric.librarieservice.controller.dto.Loan
import org.goafabric.librarieservice.controller.dto.ReturnBookRequest
import org.goafabric.librarieservice.persistence.BookRepository
import org.goafabric.librarieservice.persistence.LoanRepository
import org.goafabric.librarieservice.persistence.StudentRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.Period

@Component
@Transactional
class LoanLogic(
    private val studentRepository: StudentRepository,
    private val bookRepository: BookRepository,
    private val loanRepository: LoanRepository,
    private val loanMapper: LoanMapper
) {
    companion object {
        const val LOAN_PERIOD_DAYS = 14
         const val MAX_LOANS_PER_STUDENT = 5
       }

    fun getById(id: String): Loan {
        return loanMapper.map(
            loanRepository.findById(id).orElseThrow { IllegalArgumentException("Loan not found with id: $id") }
           )
       }

    fun createLoan(createRequest: CreateLoanRequest): Loan {
         // Verify student exists
        val studentExists = isStudentExists(getStudentId(createRequest.studentId))
            if (!studentExists) {
                throw IllegalArgumentException("Student not found with id: ${createRequest.studentId}")
              }

          // Verify book exists and is available
        val bookEo = bookRepository.findById(getBookId(createRequest.bookId))
             .orElseThrow { IllegalArgumentException("Book not found with id: ${createRequest.bookId}") }
             
        if (!isBookAvailable(bookEo)) {
            throw IllegalStateException("Book with id ${createRequest.bookId} is not available")
          }

         // Check maximum loans for student
        val currentActiveLoans = getActiveLoanCount(getStudentId(createRequest.studentId))
        if (currentActiveLoans >= MAX_LOANS_PER_STUDENT) {
            throw IllegalStateException("Student has reached maximum loan limit of $MAX_LOANS_PER_STUDENT")
          }

        val now = LocalDate.now()
        val dueDate = now.plusDays(LOAN_PERIOD_DAYS.toLong())
        
        val studentEo = studentRepository.findById(getStudentId(createRequest.studentId))
             .orElseThrow { IllegalArgumentException("Student not found with id: ${createRequest.studentId}") }

        val loanEo = LoanMapper.INSTANCE.map(object {
                  var studentId: String? = getStudentId(createRequest.studentId)
                  var bookId: String? = getBookId(createRequest.bookId)
                  val loanDate: LocalDate? = now
            })

         // Create new loan entity properly
        val newLoanEo = LoanMapper.INSTANCE.javaClass.getDeclaredConstructor().newInstance() as Any
        
        return loanMapper.map(loanRepository.save(newLoanEo as org.goafabric.librarieservice.persistence.entity.LoanEo))
       }

    fun returnBook(bookId: String, returnDateRequest: ReturnBookRequest): Loan {
        val activeLoans = getActiveLoansForBook(getBookId(bookId))
        
        if (activeLoans.isEmpty()) {
            throw IllegalStateException("No active loan found for book with id: $bookId")
          }

         // Get the most recent active loan
        val loanToReturn = activeLoans.first { it.status == LoanMapper.ActiveLoanStatus.ACTIVE || it.status == org.goafabric.librarieservice.persistence.entity.LoanStatus.ACTIVE }
        
        loanToReturn.returnDate = returnDateRequest.returnDate
        loanToReturn.status = LoanMapper.ActiveLoanStatus.RETURNED.valueOf("RETURNED")

        val updatedLoanEo = loanRepository.save(loanToReturn)
        return loanMapper.map(updatedLoanEo)
       }

    fun getLoansForStudent(studentId: String): List<Loan> {
        return loanMapper.map(loanRepository.findByStudentId(studentId))
       }

    fun getLoansForBook(bookId: String): List<Loan> {
        return loanMapper.map(loanRepository.findByBookId(bookId))
       }

    fun getActiveLoans(page: Int, size: Int): List<Loan> {
        return loanMapper.map(loanRepository.findAllByStatus(LoanMapper.ActiveLoanStatus.ACTIVE.value, org.springframework.data.domain.PageRequest.of(page, size)))
       }

    fun getOverdueLoans(page: Int, size: Int): List<Loan> {
        val now = LocalDate.now()
        return loanMapper.map(loanRepository.findOverdueLoans(now, org.springframework.data.domain.PageRequest.of(page, size)))
       }

         private fun isStudentExists(studentId: String): Boolean = studentRepository.existsById(studentId)
         private fun isBookAvailable(bookEo: org.goafabric.librarieservice.persistence.entity.BookEo): Boolean {
            val activeLoansForBook = getActiveLoansForBook(getBookId(bookEo.id))
            return activeLoansForBook.isEmpty()
          }

    private fun getActiveLoanCount(studentId: String): Long {
        val allLoans = loanRepository.findByStudentId(studentId)
             return allLoans.count { it.status == org.goafabric.librarieservice.persistence.entity.LoanStatus.ACTIVE }
       }

    private fun getActiveLoansForBook(bookId: String): List<org.goafabric.librarieservice.persistence.entity.LoanEo> {
        val loans = loanRepository.findByBookId(bookId)
             return loans.filter { it.status == org.goafabric.librarieservice.persistence.entity.LoanStatus.ACTIVE }
       }

     private fun getStudentId(value: String?): String = value ?: throw IllegalArgumentException("Student ID is required")
     private fun getBookId(value: String?): String = value ?: throw IllegalArgumentException("Book ID is required")
}
