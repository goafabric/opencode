package org.goafabric.libraryservice.logic

import org.goafabric.libraryservice.controller.dto.Library
import org.goafabric.libraryservice.persistence.entity.LibraryEo
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface LibraryMapper {
    fun map(value: LibraryEo): Library
    fun map(value: Library): LibraryEo
    fun map(values: List<LibraryEo>): List<Library>
    fun map(values: Iterable<LibraryEo>): List<Library>
}
