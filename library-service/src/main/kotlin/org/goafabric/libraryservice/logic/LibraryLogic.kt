package org.goafabric.libraryservice.logic

import org.goafabric.libraryservice.controller.dto.Library
import org.goafabric.libraryservice.persistence.LibraryRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class LibraryLogic(
    private val libraryMapper: LibraryMapper,
    private val libraryRepository: LibraryRepository
) {
    fun getById(id: String): Library {
        return libraryMapper.map(
            libraryRepository.findById(id).orElseThrow()
        )
    }

    fun findAll(page: Int, size: Int): List<Library> {
        return libraryMapper.map(
            libraryRepository.findAll(PageRequest.of(page, size))
        )
    }

    fun findByName(name: String, page: Int, size: Int): List<Library> {
        return libraryMapper.map(
            libraryRepository.findByNameContains(name, PageRequest.of(page, size))
        )
    }

    fun save(library: Library): Library {
        return libraryMapper.map(libraryRepository.save(
            libraryMapper.map(library))
        )
    }

    fun deleteById(id: String) {
        libraryRepository.deleteById(id)
    }
}
