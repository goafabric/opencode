package org.goafabric.librarieservice.controller.dto

import jakarta.ws.rs.QueryParam

class BookSearch {
    @QueryParam("title")
    var title: String? = null

    @QueryParam("author")
    var author: String? = null

    @QueryParam("isbn")
    var isbn: String? = null

    @QueryParam("studentId")
    var studentId: String? = null

    constructor()

    constructor(title: String?, author: String?, isbn: String?, studentId: String?) {
        this.title = title
        this.author = author
        this.isbn = isbn
        this.studentId = studentId
    }
}
