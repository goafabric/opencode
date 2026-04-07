package org.goafabric.librarieservice.extensions

import jakarta.validation.ConstraintViolationException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

internal class ExceptionHandlerTest {
    private val exceptionHandler = ExceptionHandler()

       @Test
    fun testHandleIllegalArgumentException() {
           val ex = IllegalArgumentException("Invalid input")
          
         val response = exceptionHandler.handleIllegalArgumentException(ex)
        
        assertThat(response).isNotNull
      assertThat(response.statusCode.value()).isEqualTo(HttpStatus.PRECONDITION_FAILED.value())
      }

      @Test
    fun testHandleIllegalStateException() {
          val ex = IllegalStateException("Business rule violation")
         
       val response = exceptionHandler.handleIllegalStateException(ex)
      
      assertThat(response).isNotNull
     assertThat(response.statusCode.value()).isEqualTo(HttpStatus.PRECONDITION_FAILED.value())
        }

      @Test
    fun testHandleConstraintValidationException() {
           val ex = ConstraintViolationException("Validation failed", mutableSetOf())
        
        val response = exceptionHandler.handleConstraintValidationException(ex)
       
         assertThat(response).isNotNull
        assertThat(response.statusCode.value()).isEqualTo(HttpStatus.PRECONDITION_FAILED.value())
          }

       @Test
    fun testHandleGeneralException() {
           val ex = RuntimeException("Unexpected error")
          
        val response = exceptionHandler.handleGeneralException(ex)
        
        assertThat(response).isNotNull
       assertThat(response.statusCode.value()).isEqualTo(HttpStatus.BAD_REQUEST.value())
              }

      companion object {
          private fun assertThat(value: Any?) = org.assertj.core.api.Assertions.assertThat(value)
            private fun assertThat(value: Int) = org.assertj.core.api.Assertions.assertThat(value)
            private fun assertThat(value: String) = org.assertj.core.api.Assertions.assertThat(value)
         }
}
