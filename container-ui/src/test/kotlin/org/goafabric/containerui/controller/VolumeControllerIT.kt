package org.goafabric.containerui.controller

import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.goafabric.containerui.controller.dto.Volume
import org.goafabric.containerui.logic.VolumeLogic
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@QuarkusTest
class VolumeControllerIT {

    companion object {
        val mockLogic: VolumeLogic = mock(VolumeLogic::class.java)

        @JvmStatic
        @BeforeAll
        fun setup() {
            QuarkusMock.installMockForType(mockLogic, VolumeLogic::class.java)
        }
    }

    @Test
    fun `GET volumes returns list`() {
        val volume = Volume(
            name = "my-volume",
            created = "2024-01-01 10:00",
            size = "–"
        )
        `when`(mockLogic.listVolumes()).thenReturn(listOf(volume))

        given()
            .`when`().get("/api/volumes")
            .then()
            .statusCode(200)
            .body("[0].name", equalTo("my-volume"))
    }

    @Test
    fun `DELETE volume returns 204`() {
        given()
            .`when`().delete("/api/volumes/my-volume")
            .then()
            .statusCode(204)
    }
}
