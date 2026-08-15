package org.goafabric.librarieservice.extensions

import jakarta.ws.rs.core.Response
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ExceptionHandlerTest {
    private val exceptionHandler: ExceptionHandler = ExceptionHandler()

     @Test
    fun handleIllegalArgumentException() {
        val response = exceptionHandler.toResponse(IllegalArgumentException("illegal argument"))
        assertThat(response.status).isEqualTo(Response.Status.PRECONDITION_FAILED.statusCode)
      }

     @Test
    fun handleIllegalStateException() {
        val response = exceptionHandler.toResponse(IllegalStateException("illegal state"))
        assertThat(response.status).isEqualTo(Response.Status.PRECONDITION_FAILED.statusCode)
      }

     @Test
    fun handleGeneralException() {
        val response = exceptionHandler.toResponse(NullPointerException("null pointer"))
        assertThat(response.status).isEqualTo(Response.Status.BAD_REQUEST.statusCode)
      }
}
