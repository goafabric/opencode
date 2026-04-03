package org.goafabric.libraryservice.controller.dto

import jakarta.validation.constraints.Size

data class Book (
    val id: String?,
    val title: String?,
    val author: String?,
    val isbn: String?,
    val pageCount: Int? = null,
    val publishedYear: Int? = null,
    var version: Long? = null
)

data class BookSearch(
    val id: String? = null,
    @field:Size(min = 3, max = 255) val title: String? = null,
    @field:Size(min = 3, max = 255) val author: String? = null,
    @field:Size(min = 3, max = 20) val isbn: String? = null
)
