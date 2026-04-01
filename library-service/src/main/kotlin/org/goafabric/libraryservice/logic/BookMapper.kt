package org.goafabric.libraryservice.logic

import org.goafabric.libraryservice.controller.dto.Book
import org.goafabric.libraryservice.controller.dto.BookSearch
import org.goafabric.libraryservice.persistence.entity.BookEo
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface BookMapper {
    @Mapping(source = "library.id", target = "libraryId")
    fun map(value: BookEo): Book
    fun map(values: List<BookEo>): List<Book>
    fun map(values: Iterable<BookEo>): List<Book>

    fun map(value: BookSearch): BookEo
}
