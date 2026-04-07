package org.goafabric.librarieservice.persistence

import org.goafabric.librarieservice.persistence.entity.LoanEo
import org.goafabric.librarieservice.persistence.entity.LoanStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import java.time.LocalDate

interface LoanRepository : CrudRepository<LoanEo, String> {
    
     fun findByStudentId(studentId: String): List<LoanEo>
     
     fun findByBookId(bookId: String): List<LoanEo>
     
     fun findAllByStatus(status: LoanStatus, pageable: Pageable): Page<LoanEo>
     
     fun findActiveLoansForStudent(studentId: String): List<LoanEo>
     
     fun findOverdueLoans(beforeDate: LocalDate, pageable: Pageable): Page<LoanEo>
    
     fun count(): Long
     
     fun countByStatus(status: LoanStatus): Long
}
