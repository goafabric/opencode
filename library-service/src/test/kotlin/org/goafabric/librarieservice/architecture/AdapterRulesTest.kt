package org.goafabric.librarieservice.architecture

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import org.goafabric.librarieservice.Application

@AnalyzeClasses(packagesOf = [Application::class], importOptions = [DoNotIncludeTests::class])
class AdapterRulesTest {
        @ArchTest
    val adapterName: ArchRule = ArchRuleDefinition.classes().that()
            .areMetaAnnotatedWith(RegisterRestClient::class.java)
            .should().haveSimpleNameEndingWith("Adapter")
            .allowEmptyShould(true)

        @ArchTest
    val declarativeClientShouldUserCircuitBreaker: ArchRule = ArchRuleDefinition.classes().that()
            .areMetaAnnotatedWith(RegisterRestClient::class.java)
            .should()
            .beMetaAnnotatedWith(org.eclipse.microprofile.faulttolerance.CircuitBreaker::class.java)
            .allowEmptyShould(true)

}
