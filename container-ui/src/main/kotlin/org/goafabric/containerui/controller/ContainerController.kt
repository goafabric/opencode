package org.goafabric.containerui.controller

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.goafabric.containerui.controller.dto.Container
import org.goafabric.containerui.controller.dto.ContainerLog
import org.goafabric.containerui.controller.dto.ContainerStats
import org.goafabric.containerui.logic.ContainerLogic

@Path("/api/containers")
@Produces(MediaType.APPLICATION_JSON)
class ContainerController(private val containerLogic: ContainerLogic) {

    @GET
    fun listContainers(): List<Container> {
        return containerLogic.listContainers()
    }

    @POST
    @Path("/{id}/start")
    fun startContainer(@PathParam("id") id: String): Response {
        containerLogic.startContainer(id)
        return Response.noContent().build()
    }

    @POST
    @Path("/{id}/stop")
    fun stopContainer(@PathParam("id") id: String): Response {
        containerLogic.stopContainer(id)
        return Response.noContent().build()
    }

    @DELETE
    @Path("/{id}")
    fun deleteContainer(@PathParam("id") id: String): Response {
        containerLogic.deleteContainer(id)
        return Response.noContent().build()
    }

    @GET
    @Path("/{id}/logs")
    fun getLogs(@PathParam("id") id: String): ContainerLog {
        return containerLogic.getLogs(id)
    }

    @GET
    @Path("/stats")
    fun fetchStats(): List<ContainerStats> {
        return containerLogic.fetchStats()
    }
}
