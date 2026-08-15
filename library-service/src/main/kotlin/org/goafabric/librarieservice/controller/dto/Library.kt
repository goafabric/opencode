package org.goafabric.librarieservice.controller.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.Size

data class Library(
    val id: String? = null,
    val version: Long? = null,
    @field:Size(min = 3, max = 255) val name: String,
    @field:Size(min = 3, max = 255) val city: String,
    val books: List<@Valid Book>
)
