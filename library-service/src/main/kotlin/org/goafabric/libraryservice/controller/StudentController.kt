package org.goafabric.libraryservice.controller

import jakarta.validation.Valid
import org.goafabric.libraryservice.controller.dto.Student
import org.goafabric.libraryservice.controller.dto.StudentSearch
import org.goafabric.libraryservice.logic.StudentLogic
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RequestMapping(value = ["/students"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@Validated
class StudentController(private val studentLogic: StudentLogic) {
    @GetMapping("{id}")
    fun getById(@PathVariable("id") id: String): Student {
        return studentLogic.getById(id)
    }

    @GetMapping
    fun find(
        @ModelAttribute studentSearch: StudentSearch,
        @RequestParam("page") page: Int, @RequestParam("size") size: Int
    ): List<Student> {
        return studentLogic.find(studentSearch, page, size)
    }

    @GetMapping("email")
    fun findByEmail(@RequestParam("email") email: String): Student? {
        return studentLogic.findByEmail(email)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun save(@RequestBody student: @Valid Student): Student {
        return studentLogic.save(student)
    }

    @DeleteMapping("{id}")
    fun deleteById(@PathVariable("id") id: String) {
        studentLogic.deleteById(id)
    }
}
