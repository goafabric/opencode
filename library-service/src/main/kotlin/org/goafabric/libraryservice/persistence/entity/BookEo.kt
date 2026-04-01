package org.goafabric.libraryservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.libraryservice.persistence.extensions.AuditTrailListener
import org.goafabric.libraryservice.persistence.extensions.KafkaPublisher
import org.hibernate.annotations.TenantId

@Entity
@Table(name = "book")
@EntityListeners(AuditTrailListener::class, KafkaPublisher::class)
class BookEo (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String?,

    @TenantId
    var organizationId: String?,

    var title: String?,
    var author: String?,
    var isbn: String?,

    @ManyToOne
    @JoinColumn(name = "library_id")
    var library: LibraryEo?,

    @Version
    var version: Long?
)
