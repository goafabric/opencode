package org.goafabric.librarieservice.persistence.extensions

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.goafabric.librarieservice.controller.dto.Student
import org.goafabric.librarieservice.logic.StudentLogic
import org.goafabric.librarieservice.persistence.extensions.AuditTrailListener.AuditTrail
import org.junit.jupiter.api.Test
import java.util.Objects

@QuarkusTest
class AuditTrailListenerIT {
        @Inject lateinit var studentLogic: StudentLogic
        @Inject lateinit var entityManager: EntityManager

          @Test
    fun createUpdateDeleteStudent() {
        val student = save()

        val createStudent = selectFrom("CREATE", student.id)
        assertThat(createStudent.oldValue).isNull()
        assertThat(createStudent.newValue).isNotNull()
        val createNewValue = Objects.requireNonNull(createStudent.newValue)
        assertThat(createNewValue).contains("Marge", "Simpson")

        val updateStudent = selectFrom("UPDATE", student.id)
        assertThat(updateStudent.oldValue).isNotNull()
        val updateOldValue = Objects.requireNonNull(updateStudent.oldValue)
        assertThat(updateOldValue).contains("Marge", "Simpson")
        val updateNewValue = Objects.requireNonNull(updateStudent.newValue)
        assertThat(updateNewValue).contains("updatedFirstName", "updatedLastName")

        val deleteStudent = selectFrom("DELETE", student.id)
        assertThat(deleteStudent.oldValue).isNotNull()
        assertThat(deleteStudent.newValue).isNull()
        val deleteOldValue = Objects.requireNonNull(deleteStudent.oldValue)
        assertThat(deleteOldValue).contains("updatedFirstName", "updatedLastName")
         }

    private fun selectFrom(operation: String, id: String?): AuditTrail {
        val query = entityManager.createQuery<AuditTrail>(
             "SELECT a FROM AuditTrailListener\$AuditTrail a WHERE a.objectId = :objectId AND a.operation = :operation",
            AuditTrail::class.java
         )
        query.setParameter("objectId", id)
        query.setParameter("operation", AuditTrailListener.DbOperation.valueOf(operation))
        return query.getSingleResult()
         }

    fun save(): Student {
        val student = studentLogic.save(
            Student(
                null,
                null,
                  "Marge",
                  "Simpson",
                  "1A001099"
              )
           )

          //update
        studentLogic.save(
            Student(
                student.id, student.version,
                  "updatedFirstName", "updatedLastName", student.matriculationNumber
              )
           )

        studentLogic.delete(student.id!!)
        return student
         }
}
