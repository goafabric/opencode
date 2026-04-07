package org.goafabric.librarieservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.librarieservice.persistence.extensions.AuditTrailListener
import org.goafabric.librarieservice.persistence.extensions.KafkaPublisher
import org.hibernate.annotations.TenantId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Entity
@Table(name = "loan")
@Document("#{@httpInterceptor.getPrefix()}loan")
@EntityListeners(AuditTrailListener::class, KafkaPublisher::class)
class LoanEo (
       @Id
       @GeneratedValue(strategy = GenerationType.UUID)
     var id: String?,

       @TenantId
     var organizationId: String?,

       @ManyToOne(fetch = FetchType.LAZY)
       @JoinColumn(name = "student_id")
     var student: StudentEo? = null,

       @ManyToOne(fetch = FetchType.LAZY)
       @JoinColumn(name = "book_id")
     var book: BookEo? = null,

     var loanDate: LocalDate?,
     var returnDate: LocalDate?,
     var dueDate: LocalDate?,
     var status: LoanStatus = LoanStatus.ACTIVE,

       @Version //optimistic locking
     var version: Long?
)

enum class LoanStatus {
    ACTIVE, RETURNED, OVERDUE
}
