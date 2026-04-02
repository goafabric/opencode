package org.goafabric.libraryservice.logic

import org.goafabric.libraryservice.controller.dto.Student
import org.goafabric.libraryservice.controller.dto.StudentSearch
import org.goafabric.libraryservice.persistence.entity.StudentEo

class StudentMapper {
    fun map(dto: StudentSearch): StudentEo {
        return StudentEo(
            studentId = dto.studentId ?: "",
            name = dto.name ?: "",
            library = dto.library
        )
    }

    fun map(entity: StudentEo): Student {
        return Student(
            studentId = entity.studentId,
            name = entity.name,
            library = entity.library
        )
    }
}
