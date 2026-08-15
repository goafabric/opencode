package org.goafabric.librarieservice.controller.dto

import jakarta.validation.constraints.Size

data class Book(
    val id: String? = null,
    val version: Long? = null,
    @field:Size(min = 3, max = 255) val isbn: String,
    @field:Size(min = 3, max = 255) val title: String,
    @field:Size(min = 3, max = 255) val author: String,
     // the student currently holding this book, null when the book is not lent out
    val studentId: String? = null
)
