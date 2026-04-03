package org.goafabric.libraryservice.controller.dto

import org.goafabric.libraryservice.persistence.entity.AddressEo
import org.goafabric.libraryservice.persistence.entity.StudentEo
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface StudentMapping {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "studentNumber", target = "studentNumber")
    @Mapping(source = "year", target = "year")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "address", target = "address")
    fun map(value: StudentEo): Student

    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "studentNumber", target = "studentNumber")
    @Mapping(source = "year", target = "year")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "address", target = "address")
    fun map(value: Student): StudentEo
}

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface AddressMapping {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "street", target = "street")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "postcode", target = "postcode")
    fun map(value: AddressEo): Address
}
