package org.goafabric.libraryservice.controller

import org.assertj.core.api.Assertions.assertThat
import org.goafabric.libraryservice.controller.dto.Book
import org.goafabric.libraryservice.controller.dto.BookSearch
import org.goafabric.libraryservice.logic.BookLogic
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

internal class BookControllerTest {
    private val bookLogic: BookLogic = mock()
    private val bookController = BookController(bookLogic)

    @Test
    fun getById() {
        whenever(bookLogic.getById("0")).thenReturn(createBook())
        assertThat(bookController.getById("0").title).isEqualTo("Test Book")
    }

    @Test
    fun find() {
        whenever(bookLogic.find(BookSearch(title = "Test"), 0, 10)).thenReturn(listOf(createBook()))
        assertThat(bookController.find(BookSearch(title = "Test"), 0, 10)).isNotNull().isNotEmpty
    }

    @Test
    fun findByLibrary() {
        whenever(bookLogic.findByLibrary("lib1", 0, 10)).thenReturn(listOf(createBook()))
        assertThat(bookController.findByLibrary("lib1", 0, 10)).isNotNull().isNotEmpty
    }

    @Test
    fun findByIsbn() {
        whenever(bookLogic.findByIsbn("123")).thenReturn(createBook())
        assertThat(bookController.findByIsbn("123")!!.title).isEqualTo("Test Book")
    }

    @Test
    fun save() {
        assertThat(bookController.save(createBook())).isNull()
        verify(bookLogic, times(1)).save(createBook())
    }

    @Test
    fun deleteById() {
        bookController.deleteById("0")
        verify(bookLogic, times(1)).deleteById("0")
    }

    companion object {
        private fun createBook(): Book {
            return Book("0", null, "Test Book", "Test Author", "123", "lib1")
        }
    }
}
