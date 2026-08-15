package org.goafabric.librarieservice.persistence

import io.quarkus.hibernate.panache.PanacheRepository
import jakarta.data.page.Page
import jakarta.data.page.PageRequest
import jakarta.data.repository.Query
import org.goafabric.librarieservice.persistence.entity.StudentEo

interface StudentRepository : PanacheRepository.Managed<StudentEo, String> {
    @Query(
         ("SELECT s FROM StudentEo s " +
                   "WHERE (:firstName IS NULL OR s.firstName = :firstName) " +
                   "AND (:lastName IS NULL OR s.lastName = :lastName) " +
                   "AND (:matriculationNumber IS NULL OR s.matriculationNumber = :matriculationNumber)")
      )
    fun search(
        firstName: String?,
        lastName: String?,
        matriculationNumber: String?,
        pageable: PageRequest
      ): Page<StudentEo>

    fun save(studentEo: StudentEo): StudentEo {
        return session.merge(studentEo)
        }
}
