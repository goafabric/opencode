package org.goafabric.libraryservice.persistence

import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.goafabric.libraryservice.persistence.entity.StudentEo
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryManaged

/**
 * Repository for managing Student entities using PanacheRepository.Managed.
 */
@ApplicationScoped
class StudentRepository : PanacheRepositoryManaged<StudentEo, String> {
}
