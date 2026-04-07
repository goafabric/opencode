package org.goafabric.librarieservice.controller.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class Loan (
    val id:  String? = null,
    val version:  Long? = null,
        @NotNull(message = "Student id is required") val studentId: String?,
        @NotNull(message = "Book id is required") val bookId: String?,
    val loanDate: LocalDate? = null,
    val returnDate: LocalDate? = null,
    val dueDate: LocalDate? = null,
    val status: LoanStatus? = null
)

data class CreateLoanRequest (
        @NotNull(message = "Student id is required") val studentId: String?,
        @NotNull(message = "Book id is required") val bookId: String?
)

data class ReturnBookRequest(
        @Min(value = 1, message = "Return date should be a valid date") val returnDate: LocalDate
)

enum class LoanStatus {
    ACTIVE, RETURNED, OVERDUE
}
