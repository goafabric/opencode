package org.goafabric.libraryservice.controller.dto

/**
 * Represents a Book in the Library System.
 */
class Book(
    val id: String?,
    val title: String,
    val isbn: String,
    var availableCopies: Int = 0
)
