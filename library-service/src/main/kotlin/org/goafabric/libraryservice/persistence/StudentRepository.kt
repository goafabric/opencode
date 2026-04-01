package org.goafabric.libraryservice.persistence

import org.goafabric.libraryservice.persistence.entity.StudentEo
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface StudentRepository : CrudRepository<StudentEo, String> {
    fun findAll(example: Example<StudentEo>, pageable: Pageable): Page<StudentEo>
    fun findByEmail(email: String): StudentEo?
}
