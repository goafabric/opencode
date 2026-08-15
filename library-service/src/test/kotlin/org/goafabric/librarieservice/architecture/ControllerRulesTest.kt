package org.goafabric.librarieservice.architecture

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.library.Architectures
import jakarta.ws.rs.Produces
import org.goafabric.librarieservice.Application


@AnalyzeClasses(packagesOf = [Application::class], importOptions = [DoNotIncludeTests::class])
class ControllerRulesTest {
        @ArchTest
    val layerAreRespectedBasic: ArchRule = Architectures.layeredArchitecture()
            .consideringOnlyDependenciesInLayers()

            .layer("Controller").definedBy("..controller")
            .layer("Logic").definedBy("..logic..")

            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Logic").mayOnlyBeAccessedByLayers("Controller")

        @ArchTest
    val controllerNaming: ArchRule = ArchRuleDefinition.classes()
            .that().areAnnotatedWith(Produces::class.java)
            .should().haveSimpleNameEndingWith("Controller")

        @ArchTest
    val controllerClassesShouldNotEndWithDtoResultResponse: ArchRule = ArchRuleDefinition.classes()
            .that().resideInAPackage("..controller..")
            .should().haveSimpleNameNotEndingWith("Dto")
            .andShould().haveSimpleNameNotEndingWith("DTO")
            .andShould().haveSimpleNameNotEndingWith("Result")
            .andShould().haveSimpleNameNotEndingWith("Response")

}
