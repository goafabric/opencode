package org.goafabric.libraryservice.controller

import org.assertj.core.api.Assertions.assertThat
import org.goafabric.libraryservice.controller.dto.Student
import org.goafabric.libraryservice.controller.dto.StudentSearch
import org.goafabric.libraryservice.logic.StudentLogic
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

internal class StudentControllerTest {
    private val studentLogic: StudentLogic = mock()
    private val studentController = StudentController(studentLogic)

    @Test
    fun getById() {
        whenever(studentLogic.getById("0")).thenReturn(createStudent())
        assertThat(studentController.getById("0").firstName).isEqualTo("Homer")
    }

    @Test
    fun find() {
        whenever(studentLogic.find(StudentSearch(firstName = "Homer"), 0, 10)).thenReturn(listOf(createStudent()))
        assertThat(studentController.find(StudentSearch(firstName = "Homer"), 0, 10)).isNotNull().isNotEmpty
        assertThat(studentController.find(StudentSearch(firstName = "Homer"), 0, 10).first().firstName).isEqualTo("Homer")
    }

    @Test
    fun findByEmail() {
        whenever(studentLogic.findByEmail("homer@test.com")).thenReturn(createStudent())
        assertThat(studentController.findByEmail("homer@test.com")!!.firstName).isEqualTo("Homer")
    }

    @Test
    fun save() {
        assertThat(studentController.save(createStudent())).isNull()
        verify(studentLogic, times(1)).save(createStudent())
    }

    @Test
    fun deleteById() {
        studentController.deleteById("0")
        verify(studentLogic, times(1)).deleteById("0")
    }

    companion object {
        private fun createStudent(): Student {
            return Student("0", null, "Homer", "Simpson", "homer@test.com")
        }
    }
}
