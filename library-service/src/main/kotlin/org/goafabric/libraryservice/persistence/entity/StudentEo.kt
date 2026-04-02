package org.goafabric.libraryservice.persistence.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "STUDENTS")
data class StudentEo(
    @Id
    @Column(name = "student_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val studentId: String = "",
    @Column(name = "name")
    val name: String,
    @Column(name = "library")
    val library: String? = null,
    val books: List<BookEo>? = null
)

@Entity
@Table(name = "BOOKS")
data class BookEo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val bookId: Long,
    @Column(name = "title")
    val title: String,
    @Column(name = "author")
    val author: String,
    @Column(name = "isbn")
    val isbn: String,
    @Column(name = "borrowed_by")
    val borrowedBy: String? = null,
    @Column(name = "borrowed_on")
    val borrowedOn: LocalDateTime? = null,
    @Column(name = "due_date")
    val dueDate: LocalDateTime? = null,
    @Column(name = "is_borrowed")
    val isBorrowed: Boolean = false,
    @Column(name = "price")
    val price: BigDecimal = BigDecimal.ZERO
)

@Entity
@Table(name = "LIBRARIES")
data class LibraryEo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val libraryId: Long,
    @Column(name = "name")
    val name: String,
    @Column(name = "address")
    val address: String,
    @Column(name = "phone")
    val phone: String? = null,
    @Column(name = "email")
    val email: String? = null
)