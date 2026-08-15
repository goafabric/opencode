package org.goafabric.librarieservice.extensions

import io.opentelemetry.api.trace.Span
import io.opentelemetry.context.Context
import io.quarkiverse.mcp.server.McpConnection
import io.quarkiverse.mcp.server.ToolFilter
import io.quarkiverse.mcp.server.ToolManager.ToolInfo
import io.vertx.core.http.HttpServerRequest
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.container.ContainerResponseContext
import jakarta.ws.rs.container.ContainerResponseFilter
import jakarta.ws.rs.ext.Provider
import org.jboss.resteasy.core.interception.jaxrs.PostMatchContainerRequestContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.io.IOException

@Provider
@ApplicationScoped
class HttpInterceptor : ContainerRequestFilter, ContainerResponseFilter, ToolFilter {
    private val log: Logger = LoggerFactory.getLogger("HttpInterceptor")

     @Throws(IOException::class)
    override fun filter(request: ContainerRequestContext) {
        UserContext.setContext(request)
        configureLogsAndTracing()
        if (request is PostMatchContainerRequestContext) {
            val method = request.getResourceMethod().getMethod()
            log.info("{} http call for user {} ", method.declaringClass.getName() + "." + method.name, UserContext.userName)
         }
    }

     @Throws(IOException::class)
    override fun filter(
        containerRequestContext: ContainerRequestContext?,
        containerResponseContext: ContainerResponseContext?
     ) {
        UserContext.removeContext()
        MDC.remove("tenantId")
     }

     @Inject
    lateinit var serverRequest: HttpServerRequest

    override fun test(tool: ToolInfo, connection: McpConnection): Boolean {
        UserContext.setContext(serverRequest)
        configureLogsAndTracing()
        log.info("{} mcp call for user {} ", tool.name(), UserContext.userName)
        return true
     }

    private fun configureLogsAndTracing() {
        MDC.put("tenantId", UserContext.tenantId)
        Span.fromContext(Context.current()).setAttribute("tenant.id", UserContext.tenantId)
     }

}
