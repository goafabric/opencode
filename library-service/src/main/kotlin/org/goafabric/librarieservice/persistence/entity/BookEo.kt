package org.goafabric.librarieservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.librarieservice.persistence.extensions.AuditTrailListener
import org.goafabric.librarieservice.persistence.extensions.KafkaPublisher

@Entity
@Table(name = "book")
@EntityListeners(AuditTrailListener::class, KafkaPublisher::class)
class BookEo(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String?,

    var isbn: String,
    var title: String,
    var author: String,

    // currently holding student, a book can be lent to at most one student
    var studentId: String? = null,

    @Version //optimistic locking
    var version: Long
)
