package org.goafabric.librarieservice.logic.mapper

import org.goafabric.librarieservice.controller.dto.Book
import org.goafabric.librarieservice.controller.dto.Library
import org.goafabric.librarieservice.persistence.entity.BookEo
import org.goafabric.librarieservice.persistence.entity.LibraryEo
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface LibraryMapper {
    fun map(value: LibraryEo): Library
    fun map(value: Library): LibraryEo
    fun map(values: List<LibraryEo>): List<Library>
    fun map(values: Iterable<LibraryEo>): List<Library>

    // single item nested mapping, MapStruct auto derives the book list mapping for the library
    fun map(value: BookEo): Book
    fun map(value: Book): BookEo
}
