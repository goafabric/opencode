package org.goafabric.librarieservice.controller

import io.quarkiverse.mcp.server.Tool
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.goafabric.librarieservice.controller.dto.Student
import org.goafabric.librarieservice.controller.dto.StudentSearch
import org.goafabric.librarieservice.logic.StudentLogic

@Path("/students")
@Produces(MediaType.APPLICATION_JSON)
class StudentController(private val studentLogic: StudentLogic) {

         @GET
      @Path("/{id}")
      @Tool(name = "getStudentById", description = "get student by id")
    fun getById(@PathParam("id") id: String): Student {
        return studentLogic.getById(id)
      }

      @GET
      @Path("")
      @Tool(name = "findStudents", description = "find students")
    fun find(
         @BeanParam studentSearch: StudentSearch,
          @QueryParam("page") page: Int,
          @QueryParam("size") size: Int
      ): List<Student> {
        return studentLogic.search(studentSearch, page, size)
      }

      @POST
      @Path("")
      @Consumes(MediaType.APPLICATION_JSON)
      @Tool(name = "saveStudent", description = "save student")
    fun save(@Valid student: @Valid Student): Student {
        return studentLogic.save(student)
      }
}
