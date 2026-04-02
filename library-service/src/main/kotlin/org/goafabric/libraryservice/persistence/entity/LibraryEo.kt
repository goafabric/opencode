package org.goafabric.libraryservice.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "library")
data class LibraryEo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false, unique = true)
    val name: String,
    @OneToMany(mappedBy = "library", cascade = [CascadeType.ALL], orphanRemoval = true)
    val books: MutableSet<BookEo> = mutableSetOf()
) {
    fun addBook(book: BookEo) {
        book.library = this
        books.add(book)
    }

    fun removeBook(book: BookEo) {
        books.remove(book)
    }
}