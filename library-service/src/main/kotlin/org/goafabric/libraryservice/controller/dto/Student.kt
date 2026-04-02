package org.goafabric.libraryservice.controller.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class StudentSearch(
    @field:Size(max = 50, message = "Student id must be maximum 50 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9_-]{1,50}$", message = "Student id must be alphanumeric")
    var studentId: String? = null,
    @field:Size(max = 100, message = "Name must be maximum 100 characters")
    var name: String? = null,
    @field:Size(max = 200, message = "Library must be maximum 200 characters")
    var library: String? = null,
    @field:Positive(message = "Page must be positive")
    @field:Min(value = 0, message = "Page must be non-negative")
    var page: Int = 0,
    @field:Positive(message = "Size must be positive")
    @field:Min(value = 1, message = "Size must be at least 1")
    var size: Int = 10
)

data class Student(
    @field:Size(max = 50, message = "Student id must be maximum 50 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9_-]{1,50}$", message = "Student id must be alphanumeric")
    val studentId: String,
    @field:Size(max = 100, message = "Name must be maximum 100 characters")
    val name: String,
    @field:Size(max = 200, message = "Library must be maximum 200 characters")
    val library: String? = null,
    val books: List<Book>? = null
)