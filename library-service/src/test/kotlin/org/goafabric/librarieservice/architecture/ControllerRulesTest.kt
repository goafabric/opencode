package org.goafabric.librarieservice.architecture

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import org.goafabric.librarieservice.Application

@AnalyzeClasses(packagesOf = [Application::class], importOptions = [DoNotIncludeTests::class])
object ControllerRulesTest {

        @ArchTest
    val controllerShouldOnlyDependOnLogic: ArchRule = ArchRuleDefinition.classes()
               .that().resideInAPackage("..controller")
                 .should().onlyDependOnClassesThat()
                    .resideInAnyPackage(
                            "org.goafabric.librarieservice.controller..",
                             "org.goafabric.librarieservice.logic..",
                             "org.springframework.web.bind.annotation..",
                             "org.springframework.http..",
                             "jakarta.validation.."
                         )
                   .because("Controllers should only interact with logic layer to maintain separation of concerns")

         @ArchTest
    val controllerShouldNotDependOnPersistence: ArchRule = ArchRuleDefinition.classes()
               .that().resideInAPackage("..controller..")
                 .should().notDependOnClassesThat()
                    .resideInAPackage("..persistence..")
                   .because("Controllers should not directly interact with persistence layer")

        @ArchTest
    val controllerShouldNotDependOnEntityPackage: ArchRule = ArchRuleDefinition.classes()
               .that().resideInAPackage("..controller..")
                 .should().notDependOnClassesThat()
                    .resideInAPackage("..persistence.entity..")
                   .because("Controllers should only interact with DTOs, not entities")

        @ArchTest
    val controllerShouldImplementRestController: ArchRule = ArchRuleDefinition.classes()
               .that().resideInAPackage("..controller..").and()
                    .haveSimpleNameMatching(".*Controller$")
                 .should().beAnnotatedWith(org.springframework.web.bind.annotation.RestController::class.java)
                   .because("All controllers should be marked with @RestController")
}
