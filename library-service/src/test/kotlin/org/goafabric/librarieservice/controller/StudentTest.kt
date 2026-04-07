package org.goafabric.librarieservice.controller.dto

import org.junit.jupiter.api.Test

internal class StudentTest {
        @Test
    fun testStudentCreation() {
        val student = Student(
            id = "test-student-1", version = 1, matriculationNumber = "STU001", 
            firstName = "John", lastName = "Doe"
                )
        
          assertThat(student).isNotNull
         assertThat(student.matriculationNumber).isEqualTo("STU001")
         assertThat(student.firstName).isEqualTo("John")
        }

      @Test
    fun testStudentWithNullValues() {
             val student = Student(id = null, version = null, matriculationNumber = "STU002", 
                firstName = "Jane", lastName = "Smith"
                )
        
          assertThat(student.id).isNull()
         assertThat(student.version).isNull()
        }

       @Test
    fun testStudentSearchCreation() {
           val search = StudentSearch(matriculationNumber = "STU001", firstName = "John", lastName = null)
        
         assertThat(search.matriculationNumber).isEqualTo("STU001")
         assertThat(search.firstName).isEqualTo("John")
               }

       companion object {
            private fun assertThat(value: Any?) = org.assertj.core.api.Assertions.assertThat(value)
           }
}
