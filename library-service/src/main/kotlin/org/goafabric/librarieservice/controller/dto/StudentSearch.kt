package org.goafabric.librarieservice.controller.dto

import jakarta.ws.rs.QueryParam

class StudentSearch {
    @QueryParam("firstName")
    var firstName: String? = null

    @QueryParam("lastName")
    var lastName: String? = null

    @QueryParam("matriculationNumber")
    var matriculationNumber: String? = null

    constructor()

    constructor(firstName: String?, lastName: String?, matriculationNumber: String?) {
        this.firstName = firstName
        this.lastName = lastName
        this.matriculationNumber = matriculationNumber
    }
}
