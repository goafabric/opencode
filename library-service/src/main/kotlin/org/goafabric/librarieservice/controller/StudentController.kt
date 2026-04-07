package org.goafabric.librarieservice.controller

import jakarta.validation.Valid
import org.goafabric.librarieservice.controller.dto.Student
import org.goafabric.librarieservice.controller.dto.StudentSearch
import org.goafabric.librarieservice.logic.StudentLogic
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RequestMapping(value = ["/students"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@Validated
class StudentController(private val studentLogic: StudentLogic) {
        @GetMapping("{id}")
    fun getById(@PathVariable("id") id: String): ResponseEntity<Student> {
        return ResponseEntity.ok(studentLogic.getById(id))
        }

         //ModelAttribute automatically applies RequestParams to the GetMapping, please note that there should be indexes inside the DB for every Attribute
     @GetMapping
    fun find(
           @ModelAttribute studentSearch: StudentSearch,
           @RequestParam("page") page: Int = 0,
           @RequestParam("size") size: Int = 20
      ): ResponseEntity<List<Student>> {
        return ResponseEntity.ok(studentLogic.find(studentSearch, maxOf(page, 0), size))
       }

     @GetMapping("matriculation/{matriculationNumber}")
    fun getByMatriculationNumber(@PathVariable("matriculationNumber") matriculationNumber: String): ResponseEntity<Student> {
        return ResponseEntity.ok(studentLogic.findByMatriculationNumber(matriculationNumber))
       }

         @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
     fun create(@RequestBody student: @Valid Student): ResponseEntity<Student> {
        val created = studentLogic.save(student)
        return ResponseEntity(status = HttpStatus.CREATED, body = created)
       }

         @PutMapping("{id}")
     fun update(
            @PathVariable("id") id: String,
            @RequestBody student: @Valid Student
      ): ResponseEntity<Student> {
          // Set the ID to the path variable for update
        val studentWithId = student.copy(id = id)
        val updated = studentLogic.save(studentWithId)
        return ResponseEntity.ok(updated)
       }

         @DeleteMapping("{id}")
     fun delete(@PathVariable("id") id: String): ResponseEntity<Unit> {
        studentLogic.delete(id)
        return ResponseEntity(noContent = ok())
       }
    
    private fun ok(): Unit = Unit
}
