package org.goafabric.librarieservice.logic

import jakarta.data.page.PageRequest
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.goafabric.librarieservice.controller.dto.Book
import org.goafabric.librarieservice.controller.dto.BookSearch
import org.goafabric.librarieservice.logic.mapper.BookMapper
import org.goafabric.librarieservice.persistence.BookRepository

@Transactional
@ApplicationScoped
class BookLogic(
    private val bookMapper: BookMapper,
    private val bookRepository: BookRepository
) {

    fun getById(id: String): Book {
        return bookMapper.map(bookRepository.findById(id))
     }

    fun search(bookSearch: BookSearch, page: Int, size: Int): List<Book> {
        val books = bookRepository.search(bookSearch.title, bookSearch.author, bookSearch.isbn,
            bookSearch.studentId, PageRequest.ofPage(page.toLong() + 1, size, true))
        return bookMapper.map(books)
     }

    fun save(book: Book): Book {
        return bookMapper.map(
            bookRepository.save(
                bookMapper.map(book)))
     }

    fun delete(id: String) {
        bookRepository.deleteById(id)
     }

    // lend a book to a student, a book can be held by at most one student
    fun lend(bookId: String, studentId: String): Book {
        val book = bookRepository.findById(bookId)
        book.studentId = studentId
        return bookMapper.map(bookRepository.save(book))
     }

    // return a previously lent book, frees it for the next student
    fun returnBook(bookId: String): Book {
        val book = bookRepository.findById(bookId)
        book.studentId = null
        return bookMapper.map(bookRepository.save(book))
     }
}
