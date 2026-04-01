package org.goafabric.libraryservice.persistence

import org.goafabric.libraryservice.persistence.entity.LibraryEo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface LibraryRepository : CrudRepository<LibraryEo, String> {
    fun findByNameContains(name: String, pageable: Pageable): Page<LibraryEo>
    fun findAll(pageable: Pageable): Page<LibraryEo>
}
