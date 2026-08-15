package org.goafabric.librarieservice.architecture

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import io.quarkus.hibernate.panache.PanacheRepository
import org.goafabric.librarieservice.Application

@AnalyzeClasses(packagesOf = [Application::class], importOptions = [com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests::class])
class MapperRulesTest {
        @ArchTest
    val mappersMustNotDependOnRepositories: ArchRule = ArchRuleDefinition.noClasses()
             .that().haveSimpleNameContaining("Mapper")
            .should().dependOnClassesThat()
             .areAssignableTo(PanacheRepository::class.java)
             .because("Mappers must be stateless pure transformation components — a repository injects access to external mutable state (the database), which violates that contract. Repository access belongs in Logic classes")

        @ArchTest
    val mappersMustNotDependOnLogic: ArchRule = ArchRuleDefinition.noClasses()
             .that().haveSimpleNameContaining("Mapper")
            .should().dependOnClassesThat()
             .haveSimpleNameEndingWith("Logic")
             .because("Mappers must not depend on Logic classes — mappers are pure transformation components")
}
