package org.goafabric.librarieservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.librarieservice.persistence.extensions.AuditTrailListener
import org.goafabric.librarieservice.persistence.extensions.KafkaPublisher

@Entity
@Table(name = "library")
@EntityListeners(AuditTrailListener::class, KafkaPublisher::class)
class LibraryEo(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String?,

    var name: String,
    var city: String,

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "library_id")
    var books: MutableList<BookEo> = mutableListOf(),

    @Version //optimistic locking
    var version: Long
)
