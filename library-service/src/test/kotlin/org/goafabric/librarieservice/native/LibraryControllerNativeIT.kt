package org.goafabric.librarieservice.native

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusIntegrationTest
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ConditionEvaluationResult
import org.junit.jupiter.api.extension.ExecutionCondition
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.DockerClientFactory

class DockerAvailableCondition : ExecutionCondition {
    override fun evaluateExecutionCondition(context: ExtensionContext): ConditionEvaluationResult =
        if (DockerClientFactory.instance().isDockerAvailable)
            ConditionEvaluationResult.enabled("Docker is av") else ConditionEvaluationResult.disabled("Docker n/a")
}

@QuarkusIntegrationTest
@ExtendWith(DockerAvailableCondition::class)
@QuarkusTestResource(value = NativeTestResource::class, restrictToAnnotatedClass = true)
class LibraryControllerNativeIT {

       @Test
    fun findAll() {
        RestAssured.given()
              .`when`().get("/libraries?page=1&size=3")
              .then()
              .statusCode(200)
        }

       @Test
    fun searchBooksByTitle() {
        RestAssured.given()
              .`when`().get("/books?title=The%20Simpsons&page=1&size=3")
              .then()
              .statusCode(200)
        }

       @Test
    fun findStudentsByLastName() {
        RestAssured.given()
              .`when`().get("/students?lastName=Simpson&page=1&size=3")
              .then()
              .statusCode(200)
        }
}
