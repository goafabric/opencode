package org.goafabric.libraryservice.controller.dto

import jakarta.validation.constraints.Size

data class Student (
    val id: String? = null,
    val version: Long? = null,
    @field:Size(min = 3, max = 255) val firstName: String,
    @field:Size(min = 3, max = 255) val lastName: String,
    @field:Size(min = 3, max = 255) val email: String
)
