package org.goafabric.libraryservice.logic

import org.goafabric.libraryservice.controller.dto.BookLending
import org.goafabric.libraryservice.persistence.entity.BookLendingEo
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface BookLendingMapper {
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "book.id", target = "bookId")
    fun map(value: BookLendingEo): BookLending
    fun map(values: List<BookLendingEo>): List<BookLending>
    fun map(values: Iterable<BookLendingEo>): List<BookLending>
}
