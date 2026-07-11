package org.goafabric.containerui.controller

import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.goafabric.containerui.controller.dto.Image
import org.goafabric.containerui.logic.ImageLogic
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@QuarkusTest
class ImageControllerIT {

    companion object {
        val mockLogic: ImageLogic = mock(ImageLogic::class.java)

        @JvmStatic
        @BeforeAll
        fun setup() {
            QuarkusMock.installMockForType(mockLogic, ImageLogic::class.java)
        }
    }

    @Test
    fun `GET images returns list`() {
        val image = Image(
            id = "sha256:abc123",
            name = "nginx",
            tag = "latest",
            created = "2024-01-01 10:00",
            size = "142.00 MB"
        )
        `when`(mockLogic.listImages()).thenReturn(listOf(image))

        given()
            .`when`().get("/api/images")
            .then()
            .statusCode(200)
            .body("[0].name", equalTo("nginx"))
            .body("[0].tag", equalTo("latest"))
    }

    @Test
    fun `DELETE image returns 204`() {
        given()
            .`when`().delete("/api/images/sha256:abc123")
            .then()
            .statusCode(204)
    }
}
