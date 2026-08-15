package org.goafabric.librarieservice.controller.dto

import jakarta.ws.rs.QueryParam

class LibrarySearch {
    @QueryParam("name")
    var name: String? = null

    @QueryParam("city")
    var city: String? = null

    constructor()

    constructor(name: String?, city: String?) {
        this.name = name
        this.city = city
    }
}
