package org.goafabric.libraryservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.libraryservice.persistence.extensions.AuditTrailListener
import org.goafabric.libraryservice.persistence.extensions.TenantResolver
import org.hibernate.annotations.TenantId
import org.springframework.data.mongodb.core.mapping.Document

@Entity
@Table(name = "library")
@EntityListeners(AuditTrailListener::class)
class LibraryEo (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String?,

    @TenantId
    var organizationId: String?,

    var name: String?,
    var description: String?,
    var address: String?,
    var city: String?,

    @Version //optimistic locking
    var version: Long?
)
