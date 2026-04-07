package org.goafabric.librarieservice.extensions

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

internal class HttpInterceptorTest {
    private lateinit var interceptor: HttpInterceptor

      @BeforeEach
    fun setUp() {
        interceptor = HttpInterceptor()
        UserContext.removeContext()
         }

       @Test
     fun testPreHandleSetsUserContext() {
           val mockRequest = Mockito.mock(HttpServletRequest::class.java)
             val mockResponse = Mockito.mock(HttpServletResponse::class.java)
         
            when(mockRequest.getHeader("X-TenantId")).thenReturn("test-tenant")
          when(mockRequest.getHeader("X-OrganizationId")).thenReturn("test-org")
         when(mockRequest.getHeader("X-Auth-Request-Preferred-Username")).thenReturn("test-user")
        when(mockRequest.getHeader("X-UserInfo")).thenReturn(null)
         
           val result = interceptor.preHandle(mockRequest, mockResponse, null)
        
        assertThat(result).isTrue()
       assertThat(UserContext.tenantId).isEqualTo("test-tenant")
         assertThat(UserContext.organizationId).isEqualTo("test-org")
          assertThat(UserContext.userName).isEqualTo("test-user")

        UserContext.removeContext()
             }

         @Test
     fun testAfterCompletionRemovesContext() {
           val mockRequest = Mockito.mock(HttpServletRequest::class.java)
            val mockResponse = Mockito.mock(HttpServletResponse::class.java)
         
        UserContext.tenantId = "temp-tenant"
         interceptor.afterCompletion(mockRequest, mockResponse, null, null)
        
          assertThat(UserContext.tenantId).isNotEqualTo("temp-tenant")
            }

     companion object {
          private fun assertThat(value: Any?) = org.assertj.core.api.Assertions.assertThat(value)
         private fun assertThat(value: Boolean) = org.assertj.core.api.Assertions.assertThat(value)
             fun assertThat(value: String) = org.assertj.core.api.Assertions.assertThat(value)
       }
}
