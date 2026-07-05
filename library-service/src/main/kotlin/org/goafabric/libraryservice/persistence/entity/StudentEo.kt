package org.goafabric.libraryservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.libraryservice.persistence.extensions.AuditTrailListener
import org.goafabric.libraryservice.persistence.extensions.KafkaPublisher

@Entity
@Table(name = "student")
@EntityListeners(AuditTrailListener::class, KafkaPublisher::class)
@Cacheable
class StudentEo(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,
    val name: String,
    @Version
    var version: Long = 0
)
