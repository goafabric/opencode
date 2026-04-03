package org.goafabric.libraryservice.controller.dto

import jakarta.validation.constraints.Size

data class Library (
    val id: String?,
    val name: String? = null,
    val description: String? = null,
    val address: String? = null,
    val city: String? = null,
    var version: Long? = null
)

data class LibrarySearch(
    val id: String? = null,
    @field:Size(min = 3, max = 255) val name: String? = null,
    @field:Size(min = 3, max = 255) val description: String? = null,
    @field:Size(min = 3, max = 255) val address: String? = null,
    @field:Size(min = 3, max = 255) val city: String? = null
)
