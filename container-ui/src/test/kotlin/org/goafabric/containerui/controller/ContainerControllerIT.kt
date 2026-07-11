package org.goafabric.containerui.controller

import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.goafabric.containerui.controller.dto.Container
import org.goafabric.containerui.logic.ContainerLogic
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@QuarkusTest
class ContainerControllerIT {

    companion object {
        val mockLogic: ContainerLogic = mock(ContainerLogic::class.java)

        @JvmStatic
        @BeforeAll
        fun setup() {
            QuarkusMock.installMockForType(mockLogic, ContainerLogic::class.java)
        }
    }

    @Test
    fun `GET containers returns list`() {
        val container = Container(
            id = "abc123",
            name = "my-container",
            image = "nginx:latest",
            status = "Up 2 hours",
            state = "running",
            ports = "80/tcp",
            cpuPercent = "0.10%",
            memoryUsage = "50.00 MB"
        )
        `when`(mockLogic.listContainers()).thenReturn(listOf(container))

        given()
            .`when`().get("/api/containers")
            .then()
            .statusCode(200)
            .body("[0].name", equalTo("my-container"))
            .body("[0].image", equalTo("nginx:latest"))
            .body("[0].state", equalTo("running"))
    }

    @Test
    fun `GET containers returns empty list`() {
        `when`(mockLogic.listContainers()).thenReturn(emptyList())

        given()
            .`when`().get("/api/containers")
            .then()
            .statusCode(200)
    }

    @Test
    fun `POST start returns 204`() {
        given()
            .`when`().post("/api/containers/abc123/start")
            .then()
            .statusCode(204)
    }

    @Test
    fun `POST stop returns 204`() {
        given()
            .`when`().post("/api/containers/abc123/stop")
            .then()
            .statusCode(204)
    }

    @Test
    fun `DELETE container returns 204`() {
        given()
            .`when`().delete("/api/containers/abc123")
            .then()
            .statusCode(204)
    }
}
