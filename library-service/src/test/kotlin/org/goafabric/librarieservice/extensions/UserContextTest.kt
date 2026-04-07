package org.goafabric.librarieservice.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class UserContextTest {
       @Test
    fun testUserContextDefaultValues() {
        assertThat(UserContext.tenantId).isEqualTo("0")
        assertThat(UserContext.organizationId).isEqualTo("0")
        assertThat(UserContext.userName).isEqualTo("anonymous")
         }

      @Test
     fun testSetContextWithTenantId() {
             UserContext.tenantId = "custom-tenant"
             val context = UserContext.adapterHeaderMap
         
          assertThat(context["X-TenantId"]).isEqualTo("custom-tenant")
        UserContext.removeContext()
         }

      @Test
    fun testAdapterHeaderMapStructure() {
        UserContext.tenantId = "test-tenant"
        
         val headers = UserContext.adapterHeaderMap
        
         assertThat(headers.size).isGreaterThan(0)
        assertThat(headers["X-TenantId"]).isNotNull
        assertThat(headers["X-OrganizationId"]).isNotNull
        assertThat(headers["X-Auth-Request-Preferred-Username"]).isNotNull
        
        UserContext.removeContext()
          }

      @Test
     fun testRemoveContext() {
             UserContext.tenantId = "temp-tenant"
         val contextBeforeRemove = UserContext.tenantId
         
        assertThat(contextBeforeRemove).isEqualTo("temp-tenant")
        
       UserContext.removeContext()
        // After removal, new ThreadLocal should be created with default
          }

      companion object {
          private fun assertThat(value: Any?) = org.assertj.core.api.Assertions.assertThat(value)
          private fun assertThat(value: String) = org.assertj.core.api.Assertions.assertThat(value)
            }
}
