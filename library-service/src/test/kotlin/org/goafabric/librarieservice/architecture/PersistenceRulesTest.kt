package org.goafabric.librarieservice.architecture

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import org.goafabric.librarieservice.Application

@AnalyzeClasses(packagesOf = [Application::class], importOptions = [DoNotIncludeTests::class])
object PersistenceRulesTest {

         @ArchTest
    val entityTypeShouldHaveEoSuffix: ArchRule = ArchRuleDefinition.classes()
               .that().resideInAPackage("..persistence.entity..")
                 .and().areAnnotatedWith(jakarta.persistence.Entity::class.java)
                   .should().haveShortNameEndingWith("Eo")
                 .because("Entity classes should follow naming convention with Eo suffix")

          @ArchTest
    val repositoryShouldHaveRepositorySuffix: ArchRule = ArchRuleDefinition.classes()
               .that().resideInAPackage("..persistence..")
                    .and().haveSimpleNameNotContaining("Eo")
                 .should().haveShortNameEndingWith("Repository")
                   .because("Repository interfaces should follow naming convention with Repository suffix")

        @ArchTest
    val persistenceShouldOnlyDependOnAllowed: ArchRule = ArchRuleDefinition.classes()
               .that().resideInAPackage("..persistence..")
                 .should().onlyDependOnClassesThat()
                     .resideInAnyPackage(
                            "org.goafabric.librarieservice.persistence..",
                             "org.springframework.data..",
                             "org.hibernate..",
                             "jakarta.persistence..",
                             "java.lang",
                             "java.time"
                         )
                   .because("Persistence layer should only depend on core persistence frameworks")

         @ArchTest
    val entityShouldHaveTenantId: ArchRule = ArchRuleDefinition.fields()
               .that().resideInAPackage("..persistence.entity..")
                 .and().areAnnotatedWith(org.hibernate.annotations.TenantId::class.java)
                   .should().haveName("organizationId")
                      .because("All multi-tenant entities must have organizationId field annotated with @TenantId")

         @ArchTest
    val entityShouldHaveVersion: ArchRule = ArchRuleDefinition.classes()
               .that().resideInAPackage("..persistence.entity..")
                 .and().areAnnotatedWith(jakarta.persistence.Entity::class.java)
                   .should().haveField("version").which().isAnnotatedWith(jakarta.persistence.Version::class.java)
                     .because("All entities must have @Version field for optimistic locking")
}
