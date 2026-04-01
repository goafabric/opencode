package org.goafabric.libraryservice.logic

import org.goafabric.libraryservice.controller.dto.Student
import org.goafabric.libraryservice.controller.dto.StudentSearch
import org.goafabric.libraryservice.persistence.entity.StudentEo
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface StudentMapper {
    fun map(value: StudentEo): Student
    fun map(value: Student): StudentEo
    fun map(values: List<StudentEo>): List<Student>
    fun map(values: Iterable<StudentEo>): List<Student>

    fun map(value: StudentSearch): StudentEo
}
