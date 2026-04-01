package org.goafabric.libraryservice.controller.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class LibraryTest {
    @Test
    fun testLibrary() {
        val library = Library("1", 0, "Test Library", "Test Address")
        assertThat(library.id).isEqualTo("1")
        assertThat(library.version).isEqualTo(0)
        assertThat(library.name).isEqualTo("Test Library")
        assertThat(library.address).isEqualTo("Test Address")
    }
}
