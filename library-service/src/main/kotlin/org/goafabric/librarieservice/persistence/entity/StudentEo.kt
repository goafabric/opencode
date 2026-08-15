package org.goafabric.librarieservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.librarieservice.persistence.extensions.AuditTrailListener
import org.goafabric.librarieservice.persistence.extensions.KafkaPublisher

@Entity
@Table(name = "student")
@EntityListeners(AuditTrailListener::class, KafkaPublisher::class)
class StudentEo(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String?,

    var firstName: String,
    var lastName: String,
    var matriculationNumber: String,

    @Version //optimistic locking
    var version: Long
)
