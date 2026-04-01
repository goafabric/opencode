package org.goafabric.libraryservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.libraryservice.persistence.extensions.AuditTrailListener
import org.goafabric.libraryservice.persistence.extensions.KafkaPublisher
import org.hibernate.annotations.TenantId

@Entity
@Table(name = "student")
@EntityListeners(AuditTrailListener::class, KafkaPublisher::class)
class StudentEo (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String?,

    @TenantId
    var organizationId: String?,

    var firstName: String?,
    var lastName: String?,
    var email: String?,

    @Version
    var version: Long?
)
