package org.goafabric.libraryservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.libraryservice.persistence.extensions.AuditTrailListener
import org.goafabric.libraryservice.persistence.extensions.KafkaPublisher
import org.hibernate.annotations.TenantId

@Entity
@Table(name = "library")
@EntityListeners(AuditTrailListener::class, KafkaPublisher::class)
class LibraryEo (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String?,

    @TenantId
    var organizationId: String?,

    var name: String?,
    var address: String?,

    @Version
    var version: Long?
)
