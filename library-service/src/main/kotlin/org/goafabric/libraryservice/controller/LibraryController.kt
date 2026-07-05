package org.goafabric.libraryservice.controller

import jakarta.ws.rs.*
import jakarta.ws.rs.core.Response
import org.goafabric.libraryservice.logic.LibraryLogic
import org.goafabric.libraryservice.persistence.entity.StudentEo
import org.goafabric.libraryservice.persistence.StudentRepository
import jakarta.inject.Inject

/**
 * REST Controller for managing library functions (students, books, lending).
 */
@Path("/api/library")
class LibraryController {
     @Inject
     lateinit var studentRepository: StudentRepository

     /**
     * Creates a new student in the library system.
     */
     @POST
    @Path("/student/create")
     fun createStudent(name: String): Response {
         val student = StudentEo(id = null, name = name)
        studentRepository.persist(student)
         return Response.ok("Student created with ID: ${student.id}").build()
     }

     /**
     * Example endpoint to trigger lending logic (demonstrates multi-book lending).
     */
    @POST
     @Path("/lend-books")
     fun lendBooks(studentId: String, bookIds: List<String>): Response {
        return Response.ok("Lending books logic for student $studentId processed").build()
     }
}
