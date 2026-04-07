package org.goafabric.librarieservice.controller.dto

import jakarta.validation.constraints.Size

data class Book (
    val id:  String? = null,
    val version:  Long? = null,
        @field:Size(min = 10, max = 20) val isbn: String,
        @field:Size(min = 3, max = 500) val title: String,
        @field:Size(min = 3, max = 255) val author: String,
    val publisher: String? = null,
    val publicationYear: Int? = null,
    val available: Boolean = true
)

data class BookSearch (
    var isbn: String? = null,
    var title: String? = null,
    var author: String? = null
)
