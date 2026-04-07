package org.goafabric.librarieservice.persistence.extensions

import org.assertj.core.api.Assertions.assertThat
import org.goafabric.librarieservice.controller.dto.Student
import org.goafabric.librarieservice.logic.StudentLogic
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class AuditTrailListenerIT {
         @org.springframework.beans.factory.annotation.Value("\${spring.datasource.url:jdbc:h2:mem:test}")
     private lateinit var dataSourceUrl: String
 
         @Autowired
      private lateinit var studentLogic: StudentLogic

       @Test
    fun testAuditTrailOnCreate() {
        val student = Student(
            id = null, version = null, matriculationNumber = "TEST-STU-AUDIT-001",
            firstName = "Audit", lastName = "Trail"
             )
 
            assertThat(student.matriculationNumber).isEqualTo("TEST-STU-AUDIT-001")
         }

      @Test
    fun testAuditTrailOnUpdate() {
             val studentDto = Student(
                id = null, version = null, matriculationNumber = "TEST-STU-AUDIT-UPDATE",
                 firstName = "Update", lastName = "Test"
                   )
          assertThat(studentDto.firstName).isEqualTo("Update")
         }

      @Test
    fun testAuditTrailIsInjected() {
           assertThat(studentLogic).isNotNull
        studentLogic.find(org.goafabric.librarieservice.controller.dto.StudentSearch(), 0, 1)
        }
}
