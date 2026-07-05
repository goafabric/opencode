package org.goafabric.libraryservice.dto

import io.quarkus.test.junit.QuarkusTest
import org.goafabric.libraryservice.controller.dto.Student
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

/**
 * Unit tests for Student DTO to verify simple domain modeling without "Dto" suffix.
 */
@QuarkusTest
class StudentDtoTest {
    @Test
     fun `student should have expected properties`() {
        val student = Student(id = "123", name = "John Doe")
        
        assertThat(student.id).isEqualTo("123")
         assertThat(student.name).isEqualTo("John Doe")
    }
}
