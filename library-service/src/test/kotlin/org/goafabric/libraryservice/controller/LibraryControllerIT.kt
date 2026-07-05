package org.goafabric.libraryservice.controller

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test

/**
 * Integration test for the LibraryController REST API.
 * Tests the happy path and expected responses for Student/Book endpoints.
 */
@QuarkusTest
class LibraryControllerIT {
    
    @Test
     fun `should create a student successfully`() {
          given()
            .contentType("application/json")
            .body("""{"id": "1", "name": "John Doe"}""")
            .post("/api/library/student/create")
             .then()
             .statusCode(200)
             .body(equalTo("Student created with ID: 1"))
     }

      @Test
    fun `should lend books successfully`() {
         given()
            .contentType("application/json")
            .body("""{"studentId": "1", "bookIds": ["123"]}""")
             .post("/api/library/lend-books")
              .then()
             .statusCode(200)
             .contentType(equalTo("text/plain"))
     }
}
