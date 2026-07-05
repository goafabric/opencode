package org.goafabric.libraryservice.persistence

import jakarta.enterprise.context.ApplicationScoped
import org.goafabric.libraryservice.persistence.entity.BookEo
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryManaged

/**
 * Repository for managing Book entities using PanacheRepository.Managed.
 */
@ApplicationScoped
class BookRepository : PanacheRepositoryManaged<BookEo, String> {
}
