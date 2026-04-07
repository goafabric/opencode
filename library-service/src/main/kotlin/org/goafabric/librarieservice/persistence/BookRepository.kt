package org.goafabric.librarieservice.persistence

import org.goafabric.librarieservice.persistence.entity.BookEo
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface BookRepository : CrudRepository<BookEo, String> {
    fun findAll(example: Example<BookEo>, pageable: Pageable): Page<BookEo>
    
     fun findByIsbn(isbn: String): BookEo?
     
     fun findByTitleContainingOrAuthorContaining(title: String, author: String, pageable: Pageable): Page<BookEo>
     
     fun findByAvailable(trueValue: Boolean?): List<BookEo>
    
     fun count(): Long
}
