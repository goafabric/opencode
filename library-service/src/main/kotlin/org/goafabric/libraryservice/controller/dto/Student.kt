package org.goafabric.libraryservice.controller.dto

import jakarta.validation.constraints.Size

data class Student (
    val id: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val phoneNumber: String? = null,
    val studentNumber: String? = null,
    val year: Int? = null,
    val department: String?,
    val address: Address?,
    var version: Long? = null
)

data class Address (
    val street: String?,
    val city: String?,
    val postcode: String? = null
)
