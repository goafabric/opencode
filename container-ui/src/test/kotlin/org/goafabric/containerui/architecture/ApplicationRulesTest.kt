package org.goafabric.containerui.architecture

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test

class ApplicationRulesTest {

    private val importedClasses = ClassFileImporter()
        .importPackages("org.goafabric.containerui")

    @Test
    fun `controller should not directly access adapter`() {
        noClasses()
            .that().resideInAPackage("..controller..")
            .should().dependOnClassesThat().resideInAPackage("..adapter..")
            .check(importedClasses)
    }

    @Test
    fun `logic should not directly access controller layer (except DTOs)`() {
        noClasses()
            .that().resideInAPackage("..logic..")
            .should().dependOnClassesThat()
            .resideInAPackage("..controller")  // only the controller classes, not dto
            .check(importedClasses)
    }

    @Test
    fun `adapter should not access controller or logic`() {
        noClasses()
            .that().resideInAPackage("..adapter..")
            .should().dependOnClassesThat().resideInAnyPackage("..controller..", "..logic..")
            .check(importedClasses)
    }
}
