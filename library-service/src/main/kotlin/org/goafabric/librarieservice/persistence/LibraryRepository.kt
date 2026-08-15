package org.goafabric.librarieservice.persistence

import io.quarkus.hibernate.panache.PanacheRepository
import jakarta.data.page.Page
import jakarta.data.page.PageRequest
import jakarta.data.repository.Query
import org.goafabric.librarieservice.persistence.entity.LibraryEo

interface LibraryRepository : PanacheRepository.Managed<LibraryEo, String> {
    @Query(
         ("SELECT l FROM LibraryEo l " +
                  "WHERE (:name IS NULL OR l.name = :name) " +
                  "AND (:city IS NULL OR l.city = :city)")
     )
    fun search(
        name: String?,
        city: String?,
        pageable: PageRequest
     ): Page<LibraryEo>

    fun save(libraryEo: LibraryEo): LibraryEo {
        return session.merge(libraryEo)
    }
}
