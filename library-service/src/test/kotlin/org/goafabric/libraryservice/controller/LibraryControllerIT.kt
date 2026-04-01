package org.goafabric.libraryservice.controller

import org.assertj.core.api.Assertions.assertThat
import org.goafabric.libraryservice.controller.dto.*
import org.goafabric.libraryservice.persistence.LibraryRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class LibraryControllerIT(
    @Autowired private val libraryController: LibraryController,
    @Autowired private val libraryRepository: LibraryRepository
) {

    @Test
    fun findAll() {
        val libraries = libraryController.findAll(0, 10)
        assertThat(libraries).isNotNull().hasSize(2)
    }

    @Test
    fun findById() {
        val libraries = libraryController.findAll(0, 10)
        assertThat(libraries).isNotEmpty()

        val library = libraryController.getById(libraries.first().id!!)
        assertThat(library).isNotNull()
        assertThat(library.name).isEqualTo(libraries.first().name)
        assertThat(library.id).isNotNull()
        assertThat(library.version).isNotNull()
    }

    @Test
    fun findByName() {
        val libraries = libraryController.findByName("Central", 0, 10)
        assertThat(libraries).isNotNull().hasSize(1)
        assertThat(libraries.first().name).isEqualTo("Central City Library")
    }

    @Test
    fun save() {
        val library = libraryController.save(
            Library(name = "Test Library", address = "123 Test Street")
        )

        assertThat(library).isNotNull()
        assertThat(library.id).isNotNull()
        assertThat(library.version).isEqualTo(0)

        // update
        libraryController.save(Library(library.id, library.version, name = "Updated Library", address = library.address))
        val updated = libraryController.getById(library.id!!)
        assertThat(updated.name).isEqualTo("Updated Library")
        assertThat(updated.version).isEqualTo(1)

        libraryRepository.deleteById(library.id)
    }
}
