package org.goafabric.librarieservice.controller.dto

import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class LoanTest {
         @Test
     fun testLoanCreation() {
         val loan = Loan(
             id = "test-loan-1", version = 1, studentId = "STU001", bookId = "BOOK001",
              loanDate = LocalDate.now(), returnDate = null, dueDate = LocalDate.now().plusDays(14)
               )
         
       assertThat(loan).isNotNull
      assertThat(loan.studentId).isEqualTo("STU001")
     assertThat(loan.bookId).isEqualTo("BOOK001")
    }

      @Test
  fun testCreateLoanRequest() {
      val request = CreateLoanRequest(studentId = "STU001", bookId = "BOOK001")
      
     assertThat(request.studentId).isEqualTo("STU001")
     assertThat(request.bookId).isEqualTo("BOOK001")
    }

       @Test
  fun testReturnBookRequest() {
         val request = ReturnBookRequest(returnDate = LocalDate.now())
         
      assertThat(request.returnDate).isNotNull
    }

        companion object {
           private fun assertThat(value: Any?) = org.assertj.core.api.Assertions.assertThat(value)
         }
}
