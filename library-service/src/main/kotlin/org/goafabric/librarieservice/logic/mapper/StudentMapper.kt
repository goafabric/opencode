package org.goafabric.librarieservice.logic.mapper

import org.goafabric.librarieservice.controller.dto.Student
import org.goafabric.librarieservice.persistence.entity.StudentEo
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface StudentMapper {
    fun map(value: StudentEo): Student
    fun map(value: Student): StudentEo
    fun map(values: List<StudentEo>): List<Student>
    fun map(values: Iterable<StudentEo>): List<Student>
}
