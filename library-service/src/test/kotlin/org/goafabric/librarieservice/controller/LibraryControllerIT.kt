package org.goafabric.librarieservice.controller

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.goafabric.librarieservice.controller.dto.Book
import org.goafabric.librarieservice.controller.dto.Library
import org.goafabric.librarieservice.controller.dto.LibrarySearch
import org.goafabric.librarieservice.logic.LibraryLogic
import org.junit.jupiter.api.Test

@QuarkusTest
class LibraryControllerIT {

       @Inject
    lateinit var libraryController: LibraryController

       @Inject
    lateinit var libraryLogic: LibraryLogic

       @Test
    fun findById() {
        val libraries: List<Library> = libraryController.find(LibrarySearch(null, null), 0, 10)
        assertThat(libraries).isNotNull().hasSize(1)

        val library = libraryController.getById(libraries.first().id!!)
        assertThat(library).isNotNull()
        assertThat(library.name).isEqualTo(libraries.first().name)
        assertThat(library.books).isNotEmpty()
           }

       @Test
    fun findByCity() {
        val libraries: List<Library> = libraryController.find(LibrarySearch(null, "Springfield"), 0, 10)
        assertThat(libraries).isNotNull().hasSize(1)
        assertThat(libraries.first().city).isEqualTo("Springfield")
           }

       @Test
    fun save() {
        val library = libraryController.save(
            Library(
                null, null,
                  "Branch",
                  "Springfield",
                mutableListOf(Book(null, null, "111-222-333", "A Branch Book", "Writer", null))
             )
          )

        assertThat(library).isNotNull()
        assertThat(library.version).isEqualTo(0)

          //reload to get the persisted books of the saved library
        val reloaded = libraryController.getById(library.id!!)
        assertThat(reloaded.books).hasSize(1)

          //update
        libraryController.save(Library(library.id, library.version, "Renamed", library.city, reloaded.books))

        val updated = libraryController.find(LibrarySearch("Renamed", "Springfield"), 0, 10).first()
        assertThat(updated.version).isEqualTo(1)
        assertThat(updated.id).isEqualTo(library.id)
        assertThat(updated.name).isEqualTo("Renamed")

        libraryLogic.delete(library.id)
           }
}
