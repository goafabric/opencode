package org.goafabric.libraryservice.controller.dto

import jakarta.validation.constraints.Size

data class Book (
    val id: String? = null,
    val version: Long? = null,
    @field:Size(min = 1, max = 255) val title: String,
    @field:Size(min = 1, max = 255) val author: String,
    @field:Size(min = 1, max = 20) val isbn: String,
    val libraryId: String
)
