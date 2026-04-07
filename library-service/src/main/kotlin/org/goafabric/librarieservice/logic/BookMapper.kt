package org.goafabric.librarieservice.logic

import org.goafabric.librarieservice.controller.dto.Book
import org.goafabric.librarieservice.controller.dto.BookSearch
import org.goafabric.librarieservice.persistence.entity.BookEo
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface BookMapper {
    fun map(value: BookEo): Book
    
        @Mapping(target = "version", ignore = true)
    fun map(value: Book): BookEo
    
    fun map(values: List<BookEo>): List<Book>
    fun map(values: Iterable<BookEo>): List<Book>

    fun map(value: BookSearch): BookEo
    
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "organizationId", ignore = true)
        @Mapping(target = "version", ignore = true)
    fun updateBook(@MappingTarget target: BookEo, source: Book): void
}
