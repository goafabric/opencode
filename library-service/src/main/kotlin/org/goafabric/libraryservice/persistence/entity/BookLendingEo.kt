package org.goafabric.libraryservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.libraryservice.persistence.extensions.AuditTrailListener
import org.goafabric.libraryservice.persistence.extensions.KafkaPublisher
import org.hibernate.annotations.TenantId
import java.time.LocalDate

@Entity
@Table(name = "book_lending")
@EntityListeners(AuditTrailListener::class, KafkaPublisher::class)
class BookLendingEo (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String?,

    @TenantId
    var organizationId: String?,

    @ManyToOne
    @JoinColumn(name = "student_id")
    var student: StudentEo?,

    @ManyToOne
    @JoinColumn(name = "book_id")
    var book: BookEo?,

    var borrowDate: LocalDate?,
    var dueDate: LocalDate?,
    var returnDate: LocalDate?,

    @Version
    var version: Long?
)
