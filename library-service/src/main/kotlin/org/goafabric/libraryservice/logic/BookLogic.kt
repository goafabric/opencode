package org.goafabric.libraryservice.logic

import org.goafabric.libraryservice.controller.dto.Book
import org.goafabric.libraryservice.controller.dto.BookSearch
import org.goafabric.libraryservice.persistence.BookRepository
import org.goafabric.libraryservice.persistence.LibraryRepository
import org.goafabric.libraryservice.persistence.entity.BookEo
import org.springframework.data.domain.Example
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class BookLogic(
    private val bookMapper: BookMapper,
    private val bookRepository: BookRepository,
    private val libraryRepository: LibraryRepository
) {
    fun getById(id: String): Book {
        return bookMapper.map(
            bookRepository.findById(id).orElseThrow()
        )
    }

    fun find(bookSearch: BookSearch, page: Int, size: Int): List<Book> {
        return bookMapper.map(
            bookRepository.findAll(
                Example.of(bookMapper.map(bookSearch)),
                PageRequest.of(page, size)
            )
        )
    }

    fun findByLibrary(libraryId: String, page: Int, size: Int): List<Book> {
        return bookMapper.map(
            bookRepository.findByLibraryId(libraryId, PageRequest.of(page, size))
        )
    }

    fun findByIsbn(isbn: String): Book? {
        return bookRepository.findByIsbn(isbn)?.let { bookMapper.map(it) }
    }

    fun save(book: Book): Book {
        val library = libraryRepository.findById(book.libraryId).orElseThrow {
            IllegalArgumentException("Library with id ${book.libraryId} not found")
        }
        val bookEo = BookEo(
            id = book.id,
            organizationId = null,
            title = book.title,
            author = book.author,
            isbn = book.isbn,
            library = library,
            version = book.version
        )
        return bookMapper.map(bookRepository.save(bookEo))
    }

    fun deleteById(id: String) {
        bookRepository.deleteById(id)
    }
}
