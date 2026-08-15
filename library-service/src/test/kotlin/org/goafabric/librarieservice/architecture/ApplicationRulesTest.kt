package org.goafabric.librarieservice.architecture

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.core.importer.Location
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import org.goafabric.librarieservice.Application

@AnalyzeClasses(packagesOf = [Application::class], importOptions = [DoNotIncludeTests::class, ApplicationRulesTest.IgnoreCglib::class])
class ApplicationRulesTest {
        @ArchTest
    val librariesThatAreBanished: ArchRule = ArchRuleDefinition.noClasses()
            .should()
            .dependOnClassesThat()
            .resideInAPackage("com.google.common..")
            .orShould()
            .dependOnClassesThat()
            .resideInAPackage("org.apache.commons..")
            .because("Java 21+ and Spring cover the functionality already, managing extra libraries with transient dependencies should be avoided")

        @ArchTest
    val onlyAllowedLibraries: ArchRule = ArchRuleDefinition.classes()
            .should()
            .onlyDependOnClassesThat()
            .resideInAnyPackage(
                "org.goafabric..",
                "java..",
                "javax..",
                "jakarta..",
                "io.quarkus..", "org.jboss..",
                "org.slf4j..",
                "com.fasterxml.jackson..","tools.jackson..",
                "org.flywaydb..",
                "org.hibernate..",
                "org.mapstruct..",
                "io.github.resilience4j..",
                "io.micrometer..",
                "org.springdoc..",
                "net.ttddyy..",

                "io.swagger.v3..",
                "com.github.benmanes.caffeine..",
                "com.azure.storage.blob..",
                "software.amazon.awssdk..", "io.awspring.cloud..",

                "org.javers..",
                "com.nimbusds.jwt..",
                "tools.jackson.databind.jsontype..",

                "org.aspectj..",
                "io.opentelemetry..",

                "kotlin..",
                "kotlinx..",
                "org.jetbrains.annotations..",

                "org.eclipse.microprofile..",
                "io.mcarle.konvert..",
                "io.smallrye..", "org.apache.kafka..",
                "com.azure.core..",
                "io.quarkiverse.mcp..",
                "io.vertx.core.http.."
            )
            .because("Only core and allowed libraries should be used to avoid unnecessary third-party dependencies")

        @ArchTest
    val componentNamesThatAreBanished: ArchRule = ArchRuleDefinition.noClasses()
            .that().haveSimpleNameNotContaining("Mapper")
            .should()
            .haveSimpleNameEndingWith("Impl")
            .andShould()
            .haveSimpleNameEndingWith("Management")
            .because("Avoid filler names like Impl or Management, use neutral Bean instead")

        @ArchTest
    val flywayJavaMigrationsAreBanished: ArchRule = ArchRuleDefinition.noClasses().should().dependOnClassesThat()
            .resideInAnyPackage("org.flywaydb.core.api.migration..")
            .because("Flyway Java Migrations should not be used, complex import logic should go to a separate batch, simple ones with a simple Java class if aware of the consequences")

    internal class IgnoreCglib : ImportOption {
        override fun includes(location: Location): Boolean {
            return !location.contains("$$") && !location.contains("EnhancerByCGLIB")
            }
        }
}
