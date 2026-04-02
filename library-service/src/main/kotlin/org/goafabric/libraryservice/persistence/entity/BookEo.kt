package org.goafabric.libraryservice.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "book")
data class BookEo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false, unique = true)
    val title: String,
    @Enumerated(EnumType.STRING)
    val status: BookStatus = BookStatus.AVAILABLE,
    @ManyToMany(mappedBy = "books")
    val students: MutableSet<StudentEo> = mutableSetOf()
) {
    fun borrow(student: StudentEo) {
        students.add(student)
        status = BookStatus.BORROWED
    }

    fun returnBook() {
        students.clear()
        status = BookStatus.AVAILABLE
    }
}

enum class BookStatus(
    val value: String
) {
    AVAILABLE("available"),
    BORROWED("borrowed")
}