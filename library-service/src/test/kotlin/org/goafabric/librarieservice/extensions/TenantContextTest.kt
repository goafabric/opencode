package org.goafabric.librarieservice.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils

internal class TenantContextTest {
         private val userContext = UserContext()

        @Test
    fun testTenantIdReadWrite() {
             var tenantIdInitial: String = "initial-tenant"
         UserContext.tenantId = tenantIdInitial
        
       assertThat(UserContext.tenantId).isEqualTo(tenantIdInitial)

            // Change the tenant ID
        val newTenantId = "changed-tenant"
        UserContext.tenantId = newTenantId
        
        assertThat(UserContext.tenantId).isEqualTo(newTenantId)
         }

         @Test
     fun testOrganizationIdReadOnly() {
           UserContext.tenantId = "test-tenant"
             val orgId = UserContext.organizationId
         
        // Organization ID should return the default or configured value
       assertThat(orgId).isNotNull
      assertThat(orgId.length).isGreaterThan(0)
        }

          @Test
    fun testUserNameReadOnly() {
            UserContext.tenantId = "test-tenant"
          val userName = UserContext.userName
        
         // UserName should return the default or configured value
        assertThat(userName).isNotNull
       assertThat(userName.length).isGreaterThan(0)
           }

        companion object {
             private fun assertThat(value: Any?) = org.assertj.core.api.Assertions.assertThat(value)
              private fun assertThat(value: String) = org.assertj.core.api.Assertions.assertThat(value)
            private fun assertThat(value: Int) = org.assertj.core.api.Assertions.assertThat(value)
         }
}
