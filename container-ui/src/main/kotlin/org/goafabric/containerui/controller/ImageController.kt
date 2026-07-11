package org.goafabric.containerui.controller

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.goafabric.containerui.controller.dto.Image
import org.goafabric.containerui.logic.ImageLogic

@Path("/api/images")
@Produces(MediaType.APPLICATION_JSON)
class ImageController(private val imageLogic: ImageLogic) {

    @GET
    fun listImages(): List<Image> {
        return imageLogic.listImages()
    }

    @DELETE
    @Path("/{id}")
    fun deleteImage(@PathParam("id") id: String): Response {
        imageLogic.deleteImage(id)
        return Response.noContent().build()
    }
}
