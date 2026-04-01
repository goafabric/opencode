package org.goafabric.libraryservice.controller

import org.assertj.core.api.Assertions.assertThat
import org.goafabric.libraryservice.controller.dto.Library
import org.goafabric.libraryservice.logic.LibraryLogic
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

internal class LibraryControllerTest {
    private val libraryLogic: LibraryLogic = mock()
    private val libraryController = LibraryController(libraryLogic)

    @Test
    fun getById() {
        whenever(libraryLogic.getById("0")).thenReturn(createLibrary())
        assertThat(libraryController.getById("0").name).isEqualTo("Test Library")
    }

    @Test
    fun findAll() {
        whenever(libraryLogic.findAll(0, 10)).thenReturn(listOf(createLibrary()))
        assertThat(libraryController.findAll(0, 10)).isNotNull().isNotEmpty
    }

    @Test
    fun findByName() {
        whenever(libraryLogic.findByName("Test", 0, 10)).thenReturn(listOf(createLibrary()))
        assertThat(libraryController.findByName("Test", 0, 10)).isNotNull().isNotEmpty
        assertThat(libraryController.findByName("Test", 0, 10).first().name).isEqualTo("Test Library")
    }

    @Test
    fun save() {
        assertThat(libraryController.save(createLibrary())).isNull()
        verify(libraryLogic, times(1)).save(createLibrary())
    }

    @Test
    fun deleteById() {
        libraryController.deleteById("0")
        verify(libraryLogic, times(1)).deleteById("0")
    }

    companion object {
        private fun createLibrary(): Library {
            return Library("0", null, "Test Library", "Test Address")
        }
    }
}
