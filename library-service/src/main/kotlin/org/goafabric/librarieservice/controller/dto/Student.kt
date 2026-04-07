package org.goafabric.librarieservice.controller.dto

import jakarta.validation.constraints.Size

data class Student (
    val id:  String? = null,
    val version:  Long? = null,
        @field:Size(min = 5, max = 20) val matriculationNumber: String,
        @field:Size(min = 3, max = 255) val firstName: String,
        @field:Size(min = 3, max = 255) val lastName: String
)

data class StudentSearch (
    var matriculationNumber: String? = null,
    var firstName: String? = null,
    var lastName: String? = null
)
