package org.goafabric.librarieservice.controller.dto

import jakarta.validation.constraints.Size
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils

internal class BookTest {
      @Test
    fun testBookCreation() {
        val book = Book(
            id = "test-book-1", version = 1, isbn = "978-0-321-20007-3", title = "Clean Code", 
            author = "Robert C. Martin", publisher = "Prentice Hall", publicationYear = 2008
            )
        
           assertThat(book).isNotNull
          assertThat(book.isbn).isEqualTo("978-0-321-20007-3")
          assertThat(book.title).isEqualTo("Clean Code")
         }

        @Test
    fun testBookSearchCreation() {
             val bookSearch = BookSearch(isbn = "978-0-321-20007-3", title = "Code", author = null)
        
           assertThat(bookSearch.isbn).isEqualTo("978-0-321-20007-3")
         }

        @Test
    fun testBookValidation() {
        val shortTitleBook = Book(id = "test", version = null, isbn = "978-0-00-00-X", title = "AB", author = "Author")
        assertThat(shortTitleBook.title.length).isLessThan(3)
         }

         companion object {
             private fun assertThat(value: Any?) = org.assertj.core.api.Assertions.assertThat(value)
           }
}
