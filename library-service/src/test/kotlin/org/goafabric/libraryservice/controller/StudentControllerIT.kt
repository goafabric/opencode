package org.goafabric.libraryservice.controller

import org.assertj.core.api.Assertions.assertThat
import org.goafabric.libraryservice.controller.dto.*
import org.goafabric.libraryservice.persistence.StudentRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class StudentControllerIT(
    @Autowired private val studentController: StudentController,
    @Autowired private val studentRepository: StudentRepository
) {

    @Test
    fun findAll() {
        val students = studentController.find(StudentSearch(), 0, 10)
        assertThat(students).isNotNull().hasSize(3)
    }

    @Test
    fun findById() {
        val students = studentController.find(StudentSearch(), 0, 10)
        assertThat(students).isNotEmpty()

        val student = studentController.getById(students.first().id!!)
        assertThat(student).isNotNull()
        assertThat(student.firstName).isEqualTo(students.first().firstName)
    }

    @Test
    fun findByFirstName() {
        val students = studentController.find(StudentSearch(firstName = "Homer"), 0, 10)
        assertThat(students).isNotNull().hasSize(1)
        assertThat(students.first().firstName).isEqualTo("Homer")
        assertThat(students.first().lastName).isEqualTo("Simpson")
    }

    @Test
    fun findByEmail() {
        val student = studentController.findByEmail("lisa.simpson@springfield.edu")
        assertThat(student).isNotNull()
        assertThat(student!!.firstName).isEqualTo("Lisa")
    }

    @Test
    fun save() {
        val student = studentController.save(
            Student(firstName = "Maggie", lastName = "Simpson", email = "maggie.simpson@springfield.edu")
        )

        assertThat(student).isNotNull()
        assertThat(student.id).isNotNull()
        assertThat(student.version).isEqualTo(0)

        // update
        studentController.save(Student(student.id, student.version, firstName = student.firstName, lastName = "updated", email = student.email))
        val updated = studentController.getById(student.id!!)
        assertThat(updated.lastName).isEqualTo("updated")
        assertThat(updated.version).isEqualTo(1)

        studentRepository.deleteById(student.id)
    }
}
