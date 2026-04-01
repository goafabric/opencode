package org.goafabric.libraryservice.controller.dto

import java.time.LocalDate

data class BookLending (
    val id: String? = null,
    val version: Long? = null,
    val studentId: String,
    val bookId: String,
    val borrowDate: LocalDate,
    val dueDate: LocalDate,
    val returnDate: LocalDate? = null
)
