package org.goafabric.librarieservice.controller

import org.assertj.core.api.Assertions.assertThat
import org.goafabric.librarieservice.controller.dto.Student
import org.goafabric.librarieservice.controller.dto.StudentSearch
import org.goafabric.librarieservice.logic.StudentLogic
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

internal class StudentControllerTest {
    private val studentLogic: StudentLogic = mock()
    private val studentController = StudentController(studentLogic)
    
     @Test
    fun getById() {
        whenever(studentLogic.getById("0")).thenReturn(createStudent())
        assertThat(studentController.getById("0").body?.lastName).isEqualTo("Simpson")
         }

      @Test
    fun find() {
        whenever(studentLogic.find(StudentSearch(firstName = "Homer"), 0, 1)).thenReturn(listOf(createStudent()))
        val response = studentController.find(StudentSearch(firstName = "Homer"), 0, 1)
        assertThat(response.body).isNotNull().isNotEmpty
        assertThat(response.body?.first()?.firstName).isEqualTo("Homer")
         }

      @Test
    fun create() {
        whenever(studentLogic.save(any())).thenReturn(createStudent())
        val response = studentController.create(Student(null, null, "STU001", "Test", "User"))
        assertThat(response.statusCode.value()).isEqualTo(201)
        verify(studentLogic, times(1)).save(any())
         }

      @Test
    fun update() {
        val updatedStudent = createStudent().copy(firstName = "Updated")
        whenever(studentLogic.save(any())).thenReturn(updatedStudent)
        val response = studentController.update("1", Student(null, null, "STU001", "Updated", "User"))
        assertThat(response.statusCode.value()).isEqualTo(200)
        verify(studentLogic, times(1)).save(any())
         }

      @Test
    fun delete() {
        doNothing().whenever(studentLogic).delete("1")
        val response = studentController.delete("1")
        assertThat(response.statusCode.value()).isEqualTo(204)
        verify(studentLogic, times(1)).delete("1")
         }

      @Test
    fun getByMatriculationNumber() {
        whenever(studentLogic.findByMatriculationNumber("STU001")).thenReturn(createStudent())
        val response = studentController.getByMatriculationNumber("STU001")
        assertThat(response.statusCode.value()).isEqualTo(200)
        verify(studentLogic, times(1)).findByMatriculationNumber("STU001")
         }

    companion object {
        private fun createStudent(): Student {
            return Student(id = "0", version = 1, matriculationNumber = "STU001", firstName = "Homer", lastName = "Simpson")
            }
        }
}
