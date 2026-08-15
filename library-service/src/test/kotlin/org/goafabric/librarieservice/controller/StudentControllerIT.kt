package org.goafabric.librarieservice.controller

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.goafabric.librarieservice.controller.dto.Student
import org.goafabric.librarieservice.controller.dto.StudentSearch
import org.goafabric.librarieservice.logic.StudentLogic
import org.junit.jupiter.api.Test

@QuarkusTest
class StudentControllerIT {

       @Inject
    lateinit var studentController: StudentController

       @Inject
    lateinit var studentLogic: StudentLogic

       @Test
    fun findById() {
        val students: List<Student> = studentController.find(StudentSearch(null, null, null), 0, 10)
        assertThat(students).isNotNull().hasSize(2)

        val student = studentController.getById(students.first().id!!)
        assertThat(student).isNotNull()
        assertThat(student.firstName).isEqualTo(students.first().firstName)
        assertThat(student.lastName).isEqualTo(students.first().lastName)
          }

       @Test
    fun findAll() {
        assertThat(studentController.find(StudentSearch(null, null, null), 0, 10)).isNotNull().hasSize(2)
          }

       @Test
    fun findByLastName() {
        val students: List<Student> = studentController.find(StudentSearch(null, "Simpson", null), 0, 10)
        assertThat(students).isNotNull().hasSize(2)
        assertThat(students.first().lastName).isEqualTo("Simpson")
          }

       @Test
    fun save() {
        val student = studentController.save(
            Student(
                null, null,
                 "Marge",
                 "Simpson",
                 "1A001003"
             )
         )

        assertThat(student).isNotNull()

        val student2: Student = studentController.getById(student.id!!)
        assertThat(student2).isNotNull()
        assertThat(student.version).isEqualTo(0)

         //update
        studentController.save(Student(student.id, student.version, firstName = student.firstName, "updated", student.matriculationNumber))

         //we have to load the entity again to get the updated version, if we just use the save returned it will be incorrect
        val studentUpdated = studentController.find(StudentSearch("Marge", "updated", null), 0, 10).first()
        assertThat(studentUpdated.version).isEqualTo(1)

        assertThat(studentUpdated.id).isEqualTo(student.id)
        assertThat(studentUpdated.version).isEqualTo(1)

        assertThat(studentUpdated.lastName).isEqualTo("updated")

        studentLogic.delete(student.id)
          }
}
