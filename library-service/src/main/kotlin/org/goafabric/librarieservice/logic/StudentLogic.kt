package org.goafabric.librarieservice.logic

import jakarta.data.page.PageRequest
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.goafabric.librarieservice.controller.dto.Student
import org.goafabric.librarieservice.controller.dto.StudentSearch
import org.goafabric.librarieservice.logic.mapper.StudentMapper
import org.goafabric.librarieservice.persistence.StudentRepository

@Transactional
@ApplicationScoped
class StudentLogic(
    private val studentMapper: StudentMapper,
    private val studentRepository: StudentRepository
) {

    fun getById(id: String): Student {
        return studentMapper.map(studentRepository.findById(id))
    }

    fun search(studentSearch: StudentSearch, page: Int, size: Int): List<Student> {
        val students = studentRepository.search(studentSearch.firstName, studentSearch.lastName,
            studentSearch.matriculationNumber, PageRequest.ofPage(page.toLong() + 1, size, true))
        return studentMapper.map(students)
    }

    fun save(student: Student): Student {
        return studentMapper.map(
            studentRepository.save(
                studentMapper.map(student)))
    }

    fun delete(id: String) {
        studentRepository.deleteById(id)
     }
}
