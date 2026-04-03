package org.goafabric.libraryservice.persistence

import org.goafabric.libraryservice.persistence.entity.LibraryEo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

interface LibraryRepository : JpaRepository<LibraryEo, String> {

    fun findByCity(city: String, pageable: Pageable): Page<LibraryEo>

    fun findByCityAndNameContaining(city: String, name: String, pageable: Pageable): Page<LibraryEo>

    fun findByCityAndNameAndDescriptionContaining(city: String, name: String, description: String, pageable: Pageable): Page<LibraryEo>

    fun findByCityAndNameAndDescriptionAndAddressContaining(city: String, name: String, description: String, address: String, pageable: Pageable): Page<LibraryEo>

}

// Helper function to search by student number - implemented in application layer
fun LibraryRepository.findByStudentNumber(studentNumber: String): StudentEo? {
    val all = findAll()
    return all.firstOrNull { it.studentNumber == studentNumber }
}
