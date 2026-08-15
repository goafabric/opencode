package org.goafabric.librarieservice.logic.mapper

import org.goafabric.librarieservice.controller.dto.Book
import org.goafabric.librarieservice.persistence.entity.BookEo
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface BookMapper {
    fun map(value: BookEo): Book
    fun map(value: Book): BookEo
    fun map(values: List<BookEo>): List<Book>
    fun map(values: Iterable<BookEo>): List<Book>
}
