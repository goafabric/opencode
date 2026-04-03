package org.goafabric.libraryservice.controller.dto

import org.goafabric.libraryservice.persistence.entity.BookEo
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface BookMapping {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "isbn", target = "isbn")
    @Mapping(source = "pageCount", target = "pageCount")
    @Mapping(source = "publishedYear", target = "publishedYear")
    fun map(value: BookEo): Book

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "isbn", target = "isbn")
    @Mapping(source = "pageCount", target = "pageCount")
    @Mapping(source = "publishedYear", target = "publishedYear")
    fun map(value: Book): BookEo
}
