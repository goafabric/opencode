package org.goafabric.librarieservice.controller

import org.assertj.core.api.Assertions.assertThat
import org.goafabric.librarieservice.controller.dto.CreateLoanRequest
import org.goafabric.librarieservice.controller.dto.Loan
import org.goafabric.librarieservice.controller.dto.ReturnBookRequest
import org.goafabric.librarieservice.logic.LoanLogic
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import java.time.LocalDate

internal class LoanControllerTest {
    private val loanLogic: LoanLogic = mock()
    private val loanController = LoanController(loanLogic)
    
     @Test
    fun getById() {
        whenever(loanLogic.getById("0")).thenReturn(createLoan())
        assertThat(loanController.getById("0").body?.studentId).isEqualTo("STU001")
         }

      @Test
    fun createLoan() {
        whenever(loanLogic.createLoan(any())).thenReturn(createLoan())
        val response = loanController.createLoan(CreateLoanRequest(studentId = "STU001", bookId = "BOOK001"))
        assertThat(response.statusCode.value()).isEqualTo(201)
        verify(loanLogic, times(1)).createLoan(any())
         }

      @Test
    fun returnBook() {
        whenever(loanLogic.returnBook("BOOK001", any())).thenReturn(createLoan().copy(returnDate = LocalDate.now()))
        val response = loanController.returnBook(bookId = "BOOK001", request = ReturnBookRequest(LocalDate.now()))
        assertThat(response.statusCode.value()).isEqualTo(200)
        verify(loanLogic, times(1)).returnBook("BOOK001", any())
         }

      @Test
    fun getLoansForStudent() {
        whenever(loanLogic.getLoansForStudent("STU001")).thenReturn(listOf(createLoan()))
        val response = loanController.getLoansForStudent("STU001")
        assertThat(response.statusCode.value()).isEqualTo(200)
        verify(loanLogic, times(1)).getLoansForStudent("STU001")
         }

      @Test
    fun getLoansForBook() {
        whenever(loanLogic.getLoansForBook("BOOK001")).thenReturn(listOf(createLoan()))
        val response = loanController.getLoansForBook("BOOK001")
        assertThat(response.statusCode.value()).isEqualTo(200)
        verify(loanLogic, times(1)).getLoansForBook("BOOK001")
         }

      @Test
    fun getActiveLoans() {
        whenever(loanLogic.getActiveLoans(0, 20)).thenReturn(listOf(createLoan()))
        val response = loanController.getActiveLoans(0, 20)
        assertThat(response.statusCode.value()).isEqualTo(200)
        verify(loanLogic, times(1)).getActiveLoans(0, 20)
         }

      @Test
    fun getOverdueLoans() {
        whenever(loanLogic.getOverdueLoans(0, 20)).thenReturn(listOf(createLoan()))
        val response = loanController.getOverdueLoans(0, 20)
        assertThat(response.statusCode.value()).isEqualTo(200)
        verify(loanLogic, times(1)).getOverdueLoans(0, 20)
         }

    companion object {
        private fun createLoan(): Loan {
            return Loan(id = "0", version = 1, studentId = "STU001", bookId = "BOOK001", 
                    loanDate = LocalDate.now().minusDays(5), 
                    dueDate = LocalDate.now().plusDays(7))
            }
        }
}
