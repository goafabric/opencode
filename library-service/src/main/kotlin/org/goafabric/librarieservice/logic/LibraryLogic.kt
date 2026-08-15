package org.goafabric.librarieservice.logic

import jakarta.data.page.PageRequest
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.goafabric.librarieservice.controller.dto.Library
import org.goafabric.librarieservice.controller.dto.LibrarySearch
import org.goafabric.librarieservice.logic.mapper.LibraryMapper
import org.goafabric.librarieservice.persistence.LibraryRepository

@Transactional
@ApplicationScoped
class LibraryLogic(
    private val libraryMapper: LibraryMapper,
    private val libraryRepository: LibraryRepository
) {

    fun getById(id: String): Library {
        return libraryMapper.map(libraryRepository.findById(id))
     }

    fun search(librarySearch: LibrarySearch, page: Int, size: Int): List<Library> {
        val libraries = libraryRepository.search(librarySearch.name, librarySearch.city,
            PageRequest.ofPage(page.toLong() + 1, size, true))
        return libraryMapper.map(libraries)
     }

    fun save(library: Library): Library {
        return libraryMapper.map(
            libraryRepository.save(
                libraryMapper.map(library)))
     }

    fun delete(id: String) {
        libraryRepository.deleteById(id)
     }
}
