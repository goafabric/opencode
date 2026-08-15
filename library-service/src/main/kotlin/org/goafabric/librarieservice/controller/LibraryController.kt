package org.goafabric.librarieservice.controller

import io.quarkiverse.mcp.server.Tool
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.goafabric.librarieservice.controller.dto.Library
import org.goafabric.librarieservice.controller.dto.LibrarySearch
import org.goafabric.librarieservice.logic.LibraryLogic

@Path("/libraries")
@Produces(MediaType.APPLICATION_JSON)
class LibraryController(private val libraryLogic: LibraryLogic) {

        @GET
      @Path("/{id}")
      @Tool(name = "getLibraryById", description = "get library by id")
    fun getById(@PathParam("id") id: String): Library {
        return libraryLogic.getById(id)
      }

      @GET
      @Path("")
      @Tool(name = "findLibraries", description = "find libraries")
    fun find(
         @BeanParam librarySearch: LibrarySearch,
          @QueryParam("page") page: Int,
          @QueryParam("size") size: Int
       ): List<Library> {
        return libraryLogic.search(librarySearch, page, size)
      }

      @POST
      @Path("")
      @Consumes(MediaType.APPLICATION_JSON)
      @Tool(name = "saveLibrary", description = "save library")
    fun save(@Valid library: @Valid Library): Library {
        return libraryLogic.save(library)
      }
}
