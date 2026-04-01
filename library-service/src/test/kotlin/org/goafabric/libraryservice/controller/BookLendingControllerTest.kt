package org.goafabric.libraryservice.controller

import org.assertj.core.api.Assertions.assertThat
import org.goafabric.libraryservice.controller.dto.BookLending
import org.goafabric.libraryservice.logic.BookLendingLogic
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import java.time.LocalDate

internal class BookLendingControllerTest {
    private val bookLendingLogic: BookLendingLogic = mock()
    private val bookLendingController = BookLendingController(bookLendingLogic)

    @Test
    fun getById() {
        whenever(bookLendingLogic.getById("0")).thenReturn(createLending())
        assertThat(bookLendingController.getById("0").studentId).isEqualTo("s1")
    }

    @Test
    fun findByStudent() {
        whenever(bookLendingLogic.findByStudent("s1", 0, 10)).thenReturn(listOf(createLending()))
        assertThat(bookLendingController.findByStudent("s1", 0, 10)).isNotNull().isNotEmpty
    }

    @Test
    fun findActiveByStudent() {
        whenever(bookLendingLogic.findActiveByStudent("s1", 0, 10)).thenReturn(listOf(createLending()))
        assertThat(bookLendingController.findActiveByStudent("s1", 0, 10)).isNotNull().isNotEmpty
    }

    @Test
    fun findByBook() {
        whenever(bookLendingLogic.findByBook("b1", 0, 10)).thenReturn(listOf(createLending()))
        assertThat(bookLendingController.findByBook("b1", 0, 10)).isNotNull().isNotEmpty
    }

    @Test
    fun lendBook() {
        val dueDate = LocalDate.now().plusDays(14)
        whenever(bookLendingLogic.lendBook("s1", "b1", dueDate)).thenReturn(createLending())
        assertThat(bookLendingController.lendBook("s1", "b1", dueDate)).isNotNull()
        verify(bookLendingLogic, times(1)).lendBook("s1", "b1", dueDate)
    }

    @Test
    fun returnBook() {
        whenever(bookLendingLogic.returnBook("0")).thenReturn(createLending().copy(returnDate = LocalDate.now()))
        val returned = bookLendingController.returnBook("0")
        assertThat(returned.returnDate).isNotNull()
    }

    companion object {
        private fun createLending(): BookLending {
            return BookLending("0", null, "s1", "b1", LocalDate.now(), LocalDate.now().plusDays(14), null)
        }
    }
}
