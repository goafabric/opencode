package org.goafabric.librarieservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.librarieservice.persistence.extensions.AuditTrailListener
import org.goafabric.librarieservice.persistence.extensions.KafkaPublisher
import org.hibernate.annotations.TenantId
import org.springframework.data.mongodb.core.mapping.Document

@Entity
@Table(name = "book")
@Document("#{@httpInterceptor.getPrefix()}book")
@EntityListeners(AuditTrailListener::class, KafkaPublisher::class)
class BookEo (
       @Id
       @GeneratedValue(strategy = GenerationType.UUID)
     var id: String?,

       @TenantId
     var organizationId: String?,

     var isbn: String?,
     var title: String?,
     var author: String?,
     var publisher: String?,
     var publicationYear: Int?,

       @Version //optimistic locking
     var version: Long?
)
