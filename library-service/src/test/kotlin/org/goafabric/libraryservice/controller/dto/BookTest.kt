package org.goafabric.libraryservice.controller.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BookTest {
    @Test
    fun testBook() {
        val book = Book("1", 0, "Test Title", "Test Author", "1234567890", "lib1")
        assertThat(book.id).isEqualTo("1")
        assertThat(book.version).isEqualTo(0)
        assertThat(book.title).isEqualTo("Test Title")
        assertThat(book.author).isEqualTo("Test Author")
        assertThat(book.isbn).isEqualTo("1234567890")
        assertThat(book.libraryId).isEqualTo("lib1")
    }
}
