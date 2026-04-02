package org.goafabric.libraryservice.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "student")
data class StudentEo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false, unique = true)
    val name: String,
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "student_book",
        joinColumns = [JoinColumn(name = "student_id")],
        inverseJoinColumns = [JoinColumn(name = "book_id")]
    )
    val books: MutableSet<BookEo> = mutableSetOf()
) {
    fun addBook(book: BookEo) {
        books.add(book)
    }

    fun removeBook(book: BookEo) {
        books.remove(book)
    }
}