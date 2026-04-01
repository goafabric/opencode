package org.goafabric.libraryservice.controller.dto

import jakarta.validation.constraints.Size

data class Library (
    val id: String? = null,
    val version: Long? = null,
    @field:Size(min = 3, max = 255) val name: String,
    @field:Size(min = 3, max = 255) val address: String
)
