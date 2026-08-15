package org.goafabric.librarieservice.controller.dto

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class StudentTest {
     @Test
    fun testValidStudent() {
        val student = Student(null, null, "John", "Doe", "1A001003")
        val violations = validator!!.validate(student)
        Assertions.assertThat(violations).isEmpty()
          }

     @Test
    fun testShortFirstName() {
        val student = Student(null, null, "Jo", "Doe", "1A001003")
        Assertions.assertThat(validator!!.validate(student)).hasSize(1)
          }

     @Test
    fun testShortLastName() {
        val student = Student(null, null, "John", "Do", "1A001003")
        Assertions.assertThat(validator!!.validate(student)).hasSize(1)
          }

     @Test
    fun testShortMatriculationNumber() {
        val student = Student(null, null, "John", "Doe", "12")
        Assertions.assertThat(validator!!.validate(student)).hasSize(1)
          }

    companion object {
        private var validator: Validator? = null

             @JvmStatic
             @BeforeAll
        fun setUp() {
            val factory = Validation.buildDefaultValidatorFactory()
            validator = factory.validator
            }
          }
}
