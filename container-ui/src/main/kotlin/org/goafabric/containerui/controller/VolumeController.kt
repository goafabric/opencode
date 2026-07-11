package org.goafabric.containerui.controller

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.goafabric.containerui.controller.dto.Volume
import org.goafabric.containerui.logic.VolumeLogic

@Path("/api/volumes")
@Produces(MediaType.APPLICATION_JSON)
class VolumeController(private val volumeLogic: VolumeLogic) {

    @GET
    fun listVolumes(): List<Volume> {
        return volumeLogic.listVolumes()
    }

    @DELETE
    @Path("/{name}")
    fun deleteVolume(@PathParam("name") name: String): Response {
        volumeLogic.deleteVolume(name)
        return Response.noContent().build()
    }
}
