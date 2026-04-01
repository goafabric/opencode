package org.goafabric.libraryservice.controller.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StudentTest {
    @Test
    fun testStudent() {
        val student = Student("1", 0, "Homer", "Simpson", "homer@test.com")
        assertThat(student.id).isEqualTo("1")
        assertThat(student.version).isEqualTo(0)
        assertThat(student.firstName).isEqualTo("Homer")
        assertThat(student.lastName).isEqualTo("Simpson")
        assertThat(student.email).isEqualTo("homer@test.com")
    }
}
