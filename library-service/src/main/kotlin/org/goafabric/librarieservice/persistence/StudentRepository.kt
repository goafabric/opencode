package org.goafabric.librarieservice.persistence

import org.goafabric.librarieservice.persistence.entity.StudentEo
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface StudentRepository : CrudRepository<StudentEo, String> {
    fun findAll(example: Example<StudentEo>, pageable: Pageable): Page<StudentEo>
    
     fun findByMatriculationNumber(matriculationNumber: String): StudentEo?
     
     fun findByFirstNameContainingOrLastNameContaining(firstName: String, lastName: String, pageable: Pageable): Page<StudentEo>
     
     fun existsByMatriculationNumber(matriculationNumber: String): Boolean
    
    fun count(): Long
}
