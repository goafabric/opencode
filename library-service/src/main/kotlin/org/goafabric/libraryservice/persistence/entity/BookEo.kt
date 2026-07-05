package org.goafabric.libraryservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.libraryservice.persistence.extensions.AuditTrailListener
import org.goafabric.libraryservice.persistence.extensions.KafkaPublisher

@Entity
@Table(name = "book")
@EntityListeners(AuditTrailListener::class, KafkaPublisher::class)
@Cacheable
class BookEo(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    val title: String,
    val isbn: String,
    var availableCopies: Int = 1,
    @Version
    var version: Long = 0
)
