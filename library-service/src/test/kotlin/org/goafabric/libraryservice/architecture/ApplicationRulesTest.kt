package org.goafabric.libraryservice.architecture

import com.tngtech.archunit.core.import.JavaModelImporter
import com.tngtech.archunit.core.import.JavaImportOptions
import com.tngtech.archunit.core.import.ImportSourceList
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.SimpleBuildRule
import com.tngtech.archunit.library.Architectures
import com.tngtech.archunit.library_LAYERED.LayeredArchitecture
import org.goafabric.libraryservice.controller.*
import org.goafabric.libraryservice.logic.*
import org.goafabric.libraryservice.persistence.*

class ApplicationRulesTest {
    @org.junit.jupiter.api.Test
    fun `application should contain only one application class`() {
        val rule = {
            import com.tngtech.archunit.core.domain.JavaRuntime
            JavaModelImporter
                .importClasspathJavaClasses(JavaImportOptions().withMetadata().withJPA().withSpring())
                .filter { it.packageName == "org.goafabric.libraryservice" || it.packageName.startsWith("org.goafabric.libraryservice.") }
                .count { it.name == "Application.kt" } shouldBe 1
        }
    }
}

fun main(args: Array<String>) {
    ApplicationRulesTest().application()
}
