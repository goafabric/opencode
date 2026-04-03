package org.goafabric.libraryservice.logic

import org.goafabric.libraryservice.controller.dto.*
import org.goafabric.libraryservice.persistence.entity.*
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface LibraryMapper {
    fun map(value: LibraryEo): Library
    fun map(value: Library): LibraryEo
    fun map(values: List<LibraryEo>): List<Library>
    fun map(values: Iterable<LibraryEo>): List<Library>
    fun map(value: LibrarySearch): LibraryEo
}

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface BookMapper {
    fun map(value: BookEo): Book
    fun map(value: Book): BookEo
    fun map(values: List<BookEo>): List<Book>
    fun map(values: Iterable<BookEo>): List<Book>
    fun map(value: BookSearch): BookEo
}

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface StudentMapper {
    fun map(value: StudentEo): Student
    fun map(value: Student): StudentEo
    fun map(values: List<StudentEo>): List<Student>
    fun map(values: Iterable<StudentEo>): List<Student>
    fun map(value: AddressEo): Address
}
