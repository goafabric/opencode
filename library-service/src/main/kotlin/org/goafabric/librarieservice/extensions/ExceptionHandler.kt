package org.goafabric.librarieservice.extensions

import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Provider
class ExceptionHandler : ExceptionMapper<Exception> {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun toResponse(e: Exception): Response {
        val status: Response.Status
        if (e is IllegalArgumentException) {
            status = Response.Status.PRECONDITION_FAILED
         } else if (e is IllegalStateException) {
            status = Response.Status.PRECONDITION_FAILED
         } else {
            status = Response.Status.BAD_REQUEST
         }

        log.error(e.message, e)
        return Response.status(status)
             .entity("An error occured: " + e.message).build()     }
}
