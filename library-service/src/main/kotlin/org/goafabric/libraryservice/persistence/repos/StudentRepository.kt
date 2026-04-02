package org.goafabric.libraryservice.persistence.repos

import org.goafabric.libraryservice.persistence.entity.StudentEo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface StudentRepository : JpaRepository<StudentEo, String> {
    @Query("select s from StudentEo s where s.name like %:name%")
    fun findByName(name: String, pageable: Pageable): Page<StudentEo>

    @Query("select s from StudentEo s where s.library like %:library%")
    fun findByLibrary(library: String, pageable: Pageable): Page<StudentEo>
    
    fun findAllByExample(
        example: StudentEo, 
        matcher: ExampleMatcher,
        pageable: Pageable
    ): Page<StudentEo>

    fun findByLibrary(library: String): List<StudentEo>

    fun findByStudentIdAndLibraryNot(studentId: String, library: String): List<StudentEo>
}