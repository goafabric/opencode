package org.goafabric.libraryservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.libraryservice.persistence.extensions.AuditTrailListener
import org.goafabric.libraryservice.persistence.extensions.TenantResolver
import org.hibernate.annotations.TenantId
import org.springframework.data.mongodb.core.mapping.Document

@Entity
@Table(name = "book")
@EntityListeners(AuditTrailListener::class)
class BookEo (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String?,

    @TenantId
    var organizationId: String?,

    var title: String,
    var author: String?,
    var isbn: String?,
    var pageCount: Int?,
    var publishedYear: Int?,

    @Version //optimistic locking
    var version: Long?
)