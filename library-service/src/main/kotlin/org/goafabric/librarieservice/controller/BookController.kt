package org.goafabric.librarieservice.controller

import io.quarkiverse.mcp.server.Tool
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.goafabric.librarieservice.controller.dto.Book
import org.goafabric.librarieservice.controller.dto.BookSearch
import org.goafabric.librarieservice.logic.BookLogic

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
class BookController(private val bookLogic: BookLogic) {

       @GET
      @Path("/{id}")
      @Tool(name = "getBookById", description = "get book by id")
    fun getById(@PathParam("id") id: String): Book {
        return bookLogic.getById(id)
      }

      @GET
      @Path("")
      @Tool(name = "findBooks", description = "find books")
    fun find(
        @BeanParam bookSearch: BookSearch,
         @QueryParam("page") page: Int,
          @QueryParam("size") size: Int
       ): List<Book> {
        return bookLogic.search(bookSearch, page, size)
      }

      @POST
      @Path("")
      @Consumes(MediaType.APPLICATION_JSON)
      @Tool(name = "saveBook", description = "save book")
    fun save(@Valid book: @Valid Book): Book {
        return bookLogic.save(book)
      }

      @POST
      @Path("{id}/lend")
      @Tool(name = "lendBook", description = "lend a book to a student")
    fun lend(@PathParam("id") id: String, @QueryParam("studentId") studentId: String): Book {
        return bookLogic.lend(id, studentId)
      }

      @POST
      @Path("{id}/return")
      @Tool(name = "returnBook", description = "return a previously lent book")
    fun returnBook(@PathParam("id") id: String): Book {
        return bookLogic.returnBook(id)
      }
}
