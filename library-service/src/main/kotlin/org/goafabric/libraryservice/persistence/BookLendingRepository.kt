package org.goafabric.libraryservice.persistence

import org.goafabric.libraryservice.persistence.entity.BookLendingEo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface BookLendingRepository : CrudRepository<BookLendingEo, String> {
    fun findByStudentId(studentId: String, pageable: Pageable): Page<BookLendingEo>
    fun findByBookId(bookId: String, pageable: Pageable): Page<BookLendingEo>
    fun findByStudentIdAndBookIdAndReturnDateIsNull(studentId: String, bookId: String): BookLendingEo?
    fun findByStudentIdAndReturnDateIsNull(studentId: String, pageable: Pageable): Page<BookLendingEo>
}
