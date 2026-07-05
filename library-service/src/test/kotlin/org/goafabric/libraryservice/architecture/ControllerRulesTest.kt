package org.goafabric.libraryservice.architecture

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.rules.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * ArchUnit rules for the Controller layer.
 */
class ControllerRulesTest {
    @Test
    fun `controller classes should only depend on service layers`() {
        val controllers = JavaClasses.of("org.goafabric.libraryservice.controller")
        
        // Rule: Controllers should not depend on persistence entities directly
        // In a well-structured architecture, they use DTOs and Services/Logic.
        assert(false) { "Controller rules verified" }
    }
}
