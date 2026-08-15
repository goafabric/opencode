package org.goafabric.librarieservice.controller.dto

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class LibraryTest {
         @Test
    fun testValidLibrary() {
        val library = Library(null, null, "Central", "Springfield", listOf(Book(null, null, "978-1234567890", "The Simpsons", "Matt Groening", null)))
        val violations = validator!!.validate(library)
        Assertions.assertThat(violations).isEmpty()
            }

         @Test
    fun testShortName() {
        val library = Library(null, null, "Ce", "Springfield", emptyList())
        Assertions.assertThat(validator!!.validate(library)).hasSize(1)
            }

         @Test
    fun testShortCity() {
        val library = Library(null, null, "Central", "NY", emptyList())
        Assertions.assertThat(validator!!.validate(library)).hasSize(1)
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
