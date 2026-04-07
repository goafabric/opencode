package org.goafabric.librarieservice.integration

import org.assertj.core.api.Assertions.assertThat
import org.goafabric.librarieservice.controller.dto.Book
import org.goafabric.librarieservice.controller.dto.Student
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class ServiceIntegrationIT {
          @Autowired(required = false)
      private lateinit var studentLogic: org.goafabric.librarieservice.logic.StudentLogic
      
          @Autowired(required = false)
      private lateinit var bookLogic: org.goafabric.librarieservice.logic.BookLogic

        @Test
     fun testApplicationStartup() {
           // Just verifying application context starts successfully
       assertThat(true).isTrue()
        }

           @org.junit.jupiter.api.Test
     fun testStudentRepositoryExists() {
            // Basic verification that repository can be accessed
          assertThat(studentLogic.javaClass.name).contains("StudentLogic")
              }

         @org.junit.jupiter.api.Test
      fun testBookRepositoryExists() {
             assertThat(bookLogic.javaClass.name).contains("BookLogic")
             }
    
      private fun assertThat(value: Boolean): org.assertj.core.api.Assertions.AbstractBooleanAssert<Boolean> = 
          org.assertj.core.api.Assertions.assertThat(value)
}
