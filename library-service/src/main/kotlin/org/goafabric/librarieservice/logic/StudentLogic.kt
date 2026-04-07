package org.goafabric.librarieservice.logic

import org.goafabric.librarieservice.controller.dto.Student
import org.goafabric.librarieservice.controller.dto.StudentSearch
import org.goafabric.librarieservice.persistence.StudentRepository
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
            studentRepository.findById(id).orElseThrow { IllegalArgumentException("Student not found with id: $id") }
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

    fun findByMatriculationNumber(matriculationNumber: String): Student {
        val found = studentRepository.findByMatriculationNumber(matriculationNumber)
            ?: throw IllegalArgumentException("Student not found with matriculation number: $matriculationNumber")
        return studentMapper.map(found)
      }

    fun findByFirstNameOrLastName(firstName: String, lastName: String, page: Int, size: Int): List<Student> {
        return studentMapper.map(
            studentRepository.findByFirstNameContainingOrLastNameContaining(firstName, lastName, PageRequest.of(page, size))
           )
      }

    fun save(student : Student): Student {
         // Check if student with matriculation number already exists
        if (student.matriculationNumber != null) {
             val existing = studentRepository.findByMatriculationNumber(student.matriculationNumber)
            if (existing != null && existing.id != student.id) {
                throw IllegalArgumentException("Student with matriculation number ${student.matriculationNumber} already exists")
               }
           }
             
        val saved = studentRepository.save(
            studentMapper.map(student))
        return studentMapper.map(saved)
          }

    fun delete(id: String) {
        if (!studentRepository.existsById(id)) {
            throw IllegalArgumentException("Student not found with id: $id")
           }
        val studentEo = studentMapper.map(
            studentRepository.findById(id).orElseThrow())
        
        // Check if student has active loans
        val activeLoans = studentEo.loans?.filter { it.status == StudentLogic.ActiveLoanStatus.ACTIVE }
            ?: emptyList()
        if (activeLoans.isNotEmpty()) {
            throw IllegalStateException("Cannot delete student with active loans")
           }
            
        studentRepository.delete(studentEo)
      }

    companion object {
         enum class ActiveLoanStatus(val value: String) {
                ACTIVE("ACTIVE"), RETURNED("RETURNED"), OVERDUE("OVERDUE");
                companion object {
                    fun fromValue(value: String): ActiveLoanStatus = values().firstOrNull { it.value == value } ?: ACTIVE
               }
           }
      }
}
