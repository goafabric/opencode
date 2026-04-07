package org.goafabric.librarieservice.logic

import org.goafabric.librarieservice.controller.dto.Loan
import org.goafabric.librarieservice.persistence.entity.LoanEo
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface LoanMapper {
    fun map(value: LoanEo): Loan
    
        @Mapping(target = "student", source = "studentId")
        @Mapping(target = "book", source = "bookId")
        @Mapping(target = "organizationId", ignore = true)
        @Mapping(target = "version", ignore = true)
    fun map(value: Loan): LoanEo
    
    fun map(values: List<LoanEo>): List<Loan>
    fun map(values: Iterable<LoanEo>): List<Loan>

        companion object {
            val INSTANCE = LoanMapperImpl()
        }
}
