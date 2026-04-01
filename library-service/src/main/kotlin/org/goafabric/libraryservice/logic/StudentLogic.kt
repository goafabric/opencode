package org.goafabric.libraryservice.logic

import org.goafabric.libraryservice.controller.dto.Student
import org.goafabric.libraryservice.controller.dto.StudentSearch
import org.goafabric.libraryservice.persistence.StudentRepository
import org.springframework.data.domain.Example
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class StudentLogic(
    private val studentMapper: StudentMapper,
    private val studentRepository: StudentRepository
) {
    fun getById(id: String): Student {
        return studentMapper.map(
            studentRepository.findById(id).orElseThrow()
        )
    }

    fun find(studentSearch: StudentSearch, page: Int, size: Int): List<Student> {
        return studentMapper.map(
            studentRepository.findAll(
                Example.of(studentMapper.map(studentSearch)),
                PageRequest.of(page, size)
            )
        )
    }

    fun findByEmail(email: String): Student? {
        return studentRepository.findByEmail(email)?.let { studentMapper.map(it) }
    }

    fun save(student: Student): Student {
        return studentMapper.map(studentRepository.save(
            studentMapper.map(student))
        )
    }

    fun deleteById(id: String) {
        studentRepository.deleteById(id)
    }
}
