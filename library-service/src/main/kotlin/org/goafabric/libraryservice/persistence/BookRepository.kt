package org.goafabric.libraryservice.persistence

import org.goafabric.libraryservice.persistence.entity.BookEo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<BookEo, String> {

    fun findByAuthorContaining(author: String, pageable: Pageable): Page<BookEo>

    fun findByTitleContaining(title: String, pageable: Pageable): Page<BookEo>

    fun findByTitleContainingAndAuthorContaining(title: String, author: String, pageable: Pageable): Page<BookEo>

    fun findByTitleContainingAndTitleContaining(title: String, author: String, isbn: String, pageable: Pageable): Page<BookEo>

}
