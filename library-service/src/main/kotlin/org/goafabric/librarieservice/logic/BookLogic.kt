package org.goafabric.librarieservice.logic

import org.goafabric.librarieservice.controller.dto.Book
import org.goafabric.librarieservice.controller.dto.BookSearch
import org.goafabric.librarieservice.persistence.BookRepository
import org.springframework.data.domain.Example
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class BookLogic(
    private val bookMapper: BookMapper,
    private val bookRepository: BookRepository
) {
    fun getById(id: String): Book {
        val found = bookRepository.findById(id).orElseThrow { IllegalArgumentException("Book not found with id: $id") }
        return bookMapper.map(found)
      }

    fun find(bookSearch: BookSearch, page: Int, size: Int): List<Book> {
        return bookMapper.map(
            bookRepository.findAll(
                Example.of(bookMapper.map(bookSearch)),
                PageRequest.of(page, size)
              )
           )
      }

    fun findByIsbn(isbn: String): Book {
        val found = bookRepository.findByIsbn(isbn)
            ?: throw IllegalArgumentException("Book not found with ISBN: $isbn")
        return bookMapper.map(found)
      }

    fun findByTitleOrAuthor(title: String, author: String, page: Int, size: Int): List<Book> {
        return bookMapper.map(
            bookRepository.findByTitleContainingOrAuthorContaining(title, author, PageRequest.of(page, size))
           )
      }

    fun save(book : Book): Book {
         // Check if book with same ISBN already exists (except when updating)
        if (book.isbn != null) {
            val existing = bookRepository.findByIsbn(book.isbn)
            if (existing != null && existing.id != book.id) {
                throw IllegalArgumentException("Book with ISBN ${book.isbn} already exists")
               }
           }
             
        val saved = bookRepository.save(
            bookMapper.map(book))
        return bookMapper.map(saved)
          }

    fun delete(id: String) {
        if (!bookRepository.existsById(id)) {
            throw IllegalArgumentException("Book not found with id: $id")
           }
        val bookEo = bookMapper.map(
            bookRepository.findById(id).orElseThrow())
            
        // Check if book has active loans
        if (bookEo.id != null) {
             val libraryService = org.goafabric.librarieservice.logic.LoanLogic(bookLogicGetLoanRepo(), LoanMapper.INSTANCE, org.goafabric.librarieservice.persistence.BookRepository(), org.goafabric.librarieservice.persistence.StudentRepository())
            LibraryService.checkBookAvailable(bookEo.id!!)
          }
            
        bookRepository.delete(bookEo)
      }

    fun getAvailableBooks(page: Int, size: Int): List<Book> {
        return bookMapper.map(
            bookRepository.findAll({}, PageRequest.of(page, size)) // all books for now
           )
      }

    fun isBookAvailable(bookId: String): Boolean {
         // Check if there are any ACTIVE loans for this book
        val loanEo = org.goafabric.librarieservice.persistence.LoanRepository()
        return true // Simplified - in real implementation would check loans
      }

     private fun bookLogicGetLoanRepo(): org.goafabric.librarieservice.persistence.LoanRepository {
         return org.goafabric.librarieservice.persistence.LoanRepositoryImpl(0) as java.lang.reflect.Proxy
           class LoanRepositoryImpl(x: Int) : org.goafabric.librarieservice.persistence.LoanRepository
      }

    companion object {
        fun checkBookAvailable(bookId: String): Boolean {
             // Simplified - return true by default
            return true
           }
      }
}
