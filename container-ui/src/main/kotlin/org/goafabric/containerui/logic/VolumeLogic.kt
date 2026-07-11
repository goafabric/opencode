package org.goafabric.containerui.logic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.enterprise.context.ApplicationScoped
import org.goafabric.containerui.adapter.DockerSocketAdapter
import org.goafabric.containerui.controller.dto.Volume
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@ApplicationScoped
class VolumeLogic(private val dockerSocketAdapter: DockerSocketAdapter) {

    fun listVolumes(): List<Volume> {
        val json = dockerSocketAdapter.get("/volumes")
        val response = dockerSocketAdapter.parse(json, DockerVolumesResponse::class.java)

        return (response.Volumes ?: emptyList()).map { raw ->
            Volume(
                name = raw.Name ?: "",
                created = formatTimestamp(raw.CreatedAt),
                size = "–"  // Docker API does not always return size without df call
            )
        }
    }

    fun deleteVolume(name: String) {
        dockerSocketAdapter.delete("/volumes/$name")
    }

    private fun formatTimestamp(isoTimestamp: String?): String {
        if (isoTimestamp.isNullOrBlank()) return "–"
        return try {
            val dt = OffsetDateTime.parse(isoTimestamp)
            dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        } catch (e: DateTimeParseException) {
            isoTimestamp
        }
    }

    // --- Docker API raw models ---

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class DockerVolumesResponse(
        val Volumes: List<DockerVolume>? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class DockerVolume(
        val Name: String? = null,
        val CreatedAt: String? = null,
        val Mountpoint: String? = null
    )
}
