package org.goafabric.libraryservice.persistence

import org.goafabric.libraryservice.persistence.entity.StudentEo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

interface StudentRepository : JpaRepository<StudentEo, String> {

    fun findByFirstNameContaining(firstName: String, pageable: Pageable): Page<StudentEo>
    fun findByLastNameContaining(lastName: String, pageable: Pageable): Page<StudentEo>
    fun findByEmailContaining(email: String, pageable: Pageable): Page<StudentEo>
    fun findByDepartmentContaining(department: String?, pageable: Pageable): Page<StudentEo>
    fun findByDepartmentAndFirstNameContaining(department: String, firstName: String, pageable: Pageable): Page<StudentEo>

    fun findByStudentNumber(studentNumber: String): StudentEo?
    fun findByFirstNameAndLastNameAndPageable(firstName: String, lastName: String, pageable: Pageable): Page<StudentEo>
}
