package org.goafabric.libraryservice.persistence.entity

import jakarta.persistence.*
import org.goafabric.libraryservice.persistence.extensions.AuditTrailListener
import org.goafabric.libraryservice.persistence.extensions.TenantResolver
import org.hibernate.annotations.TenantId
import org.springframework.data.mongodb.core.mapping.Document

@Entity
@Table(name = "student")
@EntityListeners(AuditTrailListener::class)
class StudentEo (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String?,

    @TenantId
    var organizationId: String?,

    var firstName: String?,
    var lastName: String?,
    var email: String?,
    var phoneNumber: String?,
    var studentNumber: String?,
    var year: Int?,
    var department: String?,
    var address: AddressEo?,

    @Version //optimistic locking
    var version: Long?,

    // Books currently borrowed by this student
    val borrowingBooks: MutableSet<String> = mutableSetOf()
)

class AddressEo {
    val id: String? = null
    var student: StudentEo? = null
    var street: String? = null
    var city: String? = null
    var postcode: String? = null
}