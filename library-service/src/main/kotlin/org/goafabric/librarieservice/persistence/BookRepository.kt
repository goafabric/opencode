package org.goafabric.librarieservice.persistence

import io.quarkus.hibernate.panache.PanacheRepository
import jakarta.data.page.Page
import jakarta.data.page.PageRequest
import jakarta.data.repository.Query
import org.goafabric.librarieservice.persistence.entity.BookEo

interface BookRepository : PanacheRepository.Managed<BookEo, String> {
    @Query(
         ("SELECT b FROM BookEo b " +
                  "WHERE (:title IS NULL OR b.title = :title) " +
                  "AND (:author IS NULL OR b.author = :author) " +
                  "AND (:isbn IS NULL OR b.isbn = :isbn) " +
                  "AND (:studentId IS NULL OR b.studentId = :studentId)")
     )
    fun search(
        title: String?,
        author: String?,
        isbn: String?,
        studentId: String?,
        pageable: PageRequest
     ): Page<BookEo>

    fun save(bookEo: BookEo): BookEo {
        return session.merge(bookEo)
    }
}
