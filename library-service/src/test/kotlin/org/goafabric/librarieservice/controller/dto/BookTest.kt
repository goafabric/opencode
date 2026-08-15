package org.goafabric.librarieservice.controller.dto

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class BookTest {
     @Test
    fun testValidBook() {
        val book = Book(null, null, "978-1234567890", "The Simpsons", "Matt Groening", null)
        val violations = validator!!.validate(book)
        Assertions.assertThat(violations).isEmpty()
          }

     @Test
    fun testShortTitle() {
        val book = Book(null, null, "978-1234567890", "A", "Matt Groening", null)
        Assertions.assertThat(validator!!.validate(book)).hasSize(1)
          }

     @Test
    fun testShortAuthor() {
        val book = Book(null, null, "978-1234567890", "The Simpsons", "M", null)
        Assertions.assertThat(validator!!.validate(book)).hasSize(1)
          }

     @Test
    fun testShortIsbn() {
        val book = Book(null, null, "12", "The Simpsons", "Matt Groening", null)
        Assertions.assertThat(validator!!.validate(book)).hasSize(1)
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
