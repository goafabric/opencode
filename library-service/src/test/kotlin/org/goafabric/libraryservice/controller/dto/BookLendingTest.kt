package org.goafabric.libraryservice.controller.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class BookLendingTest {
    @Test
    fun testBookLending() {
        val today = LocalDate.now()
        val dueDate = today.plusDays(14)
        val lending = BookLending("1", 0, "s1", "b1", today, dueDate, null)
        assertThat(lending.id).isEqualTo("1")
        assertThat(lending.version).isEqualTo(0)
        assertThat(lending.studentId).isEqualTo("s1")
        assertThat(lending.bookId).isEqualTo("b1")
        assertThat(lending.borrowDate).isEqualTo(today)
        assertThat(lending.dueDate).isEqualTo(dueDate)
        assertThat(lending.returnDate).isNull()
    }
}
