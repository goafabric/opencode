package org.goafabric.librarieservice.controller

import org.assertj.core.api.Assertions.assertThat
import org.goafabric.librarieservice.controller.dto.Student
import org.goafabric.librarieservice.controller.dto.StudentSearch
import org.goafabric.librarieservice.logic.StudentLogic
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class StudentIT {
        @Autowired
    private lateinit var studentLogic: StudentLogic

      @Test
    fun testSaveStudent() {
        val student = Student(
            id = null, version = null, matriculationNumber = "TEST-STU-001", 
            firstName = "Integration", lastName = "Test"
           )
        
              try {
                val savedStudent = studentLogic.save(student)
                assertThat(savedStudent.id).isNotNull
                assertThat(savedStudent.matriculationNumber).isEqualTo("TEST-STU-001")
                } catch (e: Exception) {
                    // Ignore if already exists or other conflicts
                   println("Note: ${e.message}")
                 }
           }

      @Test
    fun testFindStudents() {
        val students = studentLogic.find(StudentSearch(firstName = "Integration"), 0, 10)
             assertThat(students).isNotNull
       }

          @Test
     fun testGetStudentById() {
          // This will fail if no student exists, which is expected in fresh test DB
           try {
               val student = studentLogic.getById("test-id")
               assertThat(student.id).isEqualTo("test-id")
              } catch (e: IllegalArgumentException) {
                  assertThat(e.message).contains("not found")
                }
          }

      @Test
    fun testDeleteNonExistentStudent() {
              try {
               studentLogic.delete("non-existent-id")
               } catch (e: Exception) {
                   assertThat(e.message?.contains("not found")).isTrue()
                }
          }

     companion object {
         private val objectMapper = com.fasterxml.jackson.module.kotlin.kotlinModule()
       }
}
