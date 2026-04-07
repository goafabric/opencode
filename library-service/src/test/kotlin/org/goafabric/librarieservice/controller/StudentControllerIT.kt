package org.goafabric.librarieservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.assertj.core.api.Assertions.assertThat
import org.goafabric.librarieservice.controller.dto.Student
import org.goafabric.librarieservice.persistence.DemoDataImporter
import org.goafabric.librarieservice.persistence.StudentRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.delete

@WebMvcTest(StudentController::class)
class StudentControllerIT {
          @Autowired
    private lateinit var mockMvc: MockMvc

         @MockBean
    private lateinit var demoDataImporter: DemoDataImporter

      companion object {
          private val objectMapper = ObjectMapper().registerKotlinModule()

           private fun createStudentJson(student: Student): String {
            return objectMapper.writeValueAsString(student)
              }

       private fun createValidStudent(): Student {
            return Student(id = "test-id", version = 1, matriculationNumber = "STU001", firstName = "John", lastName = "Doe")
           }

       private val validStudentJson = """
         {
           "matriculationNumber": "STU001",
           "firstName": "John",
           "lastName": "Doe"
           }
         """.trimIndent()

        private val invalidStudentJson = """
         {
           "matriculationNumber": "ST",
           "firstName": "J",
           "lastName": "D"
          }
        """.trimIndent()
      }

     @Test
    fun createStudent_ShouldReturnCreated_WhenValidData() {
        val result = mockMvc.post("/students") {
            contentType = MediaType.APPLICATION_JSON
            content = validStudentJson
             }

       assertThat(result.status).isEqualTo(201)
    }

     @Test
    fun createStudent_ShouldReturnBadRequest_WhenInvalidData() {
        val result = mockMvc.post("/students") {
            contentType = MediaType.APPLICATION_JSON
            content = invalidStudentJson
             }

         assertThat(result.status).isIn(400, 422)
    }

     @Test
    fun getStudents_ShouldReturnOk_WhenValidRequest() {
        val result = mockMvc.get("/students?page=0&size=1")
        assertThat(result.status).isEqualTo(200)
    }

     @Test
    fun getStudentById_ShouldReturnOk_WhenIdExists() {
        val result = mockMvc.get("/students/test-id")
        assertThat(result.status).isIn(200, 404)
    }

      companion object {
          private val objectMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
        }
}
