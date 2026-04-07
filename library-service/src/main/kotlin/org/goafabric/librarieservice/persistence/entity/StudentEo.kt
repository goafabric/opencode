package org.goafabric.librarieservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.librarieservice.persistence.extensions.AuditTrailListener
import org.goafabric.librarieservice.persistence.extensions.KafkaPublisher
import org.hibernate.annotations.TenantId
import org.springframework.data.mongodb.core.mapping.Document

@Entity
@Table(name = "student")
@Document("#{@httpInterceptor.getPrefix()}student")
@EntityListeners(AuditTrailListener::class, KafkaPublisher::class)
class StudentEo (
       @Id
       @GeneratedValue(strategy = GenerationType.UUID)
     var id: String?,

       @TenantId
     var organizationId: String?,

     var matriculationNumber: String?,
     var firstName: String?,
     var lastName: String?,

       @OneToMany(mappedBy = "student", cascade = [CascadeType.ALL])
     var loans: MutableList<LoanEo>? = mutableListOf(),

       @Version //optimistic locking
     var version: Long?
)
