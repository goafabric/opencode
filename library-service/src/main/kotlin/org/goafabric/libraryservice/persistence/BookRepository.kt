package org.goafabric.libraryservice.persistence

import org.goafabric.libraryservice.persistence.entity.BookEo
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface BookRepository : CrudRepository<BookEo, String> {
    fun findAll(example: Example<BookEo>, pageable: Pageable): Page<BookEo>
    fun findByLibraryId(libraryId: String, pageable: Pageable): Page<BookEo>
    fun findByIsbn(isbn: String): BookEo?
}
