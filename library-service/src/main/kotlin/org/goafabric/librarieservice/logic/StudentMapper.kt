package org.goafabric.librarieservice.logic

import org.goafabric.librarieservice.controller.dto.Student
import org.goafabric.librarieservice.controller.dto.StudentSearch
import org.goafabric.librarieservice.persistence.entity.LoanEo
import org.goafabric.librarieservice.persistence.entity.StudentEo
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface StudentMapper {
    fun map(value: StudentEo): Student
    
        @Mapping(target = "loans", ignore = true)
    fun map(value: Student): StudentEo
    
    fun map(values: List<StudentEo>): List<Student>
    fun map(values: Iterable<StudentEo>): List<Student>

    fun map(value: StudentSearch): StudentEo
    
        @Mapping(target = "loans", source = "loans")
    fun updateStudent(@MappingTarget target: StudentEo, source: Student): void

      companion object {
          fun convertLoansToDtos(loans: MutableList<LoanEo>?): List<org.goafabric.librarieservice.persistence.entity.LoanEo> {
              return loans?.map { it } ?: emptyList()
           }
      }
}
