package org.goafabric.containerui.logic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.enterprise.context.ApplicationScoped
import org.goafabric.containerui.adapter.DockerSocketAdapter
import org.goafabric.containerui.controller.dto.Image
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@ApplicationScoped
class ImageLogic(private val dockerSocketAdapter: DockerSocketAdapter) {

    fun listImages(): List<Image> {
        val json = dockerSocketAdapter.get("/images/json")
        val rawImages = dockerSocketAdapter.parseList(json, DockerImage::class.java)

        return rawImages.map { raw ->
            val repoTag = raw.RepoTags?.firstOrNull() ?: "<none>:<none>"
            val parts = repoTag.split(":")
            val name = parts.getOrElse(0) { "<none>" }
            val tag = parts.getOrElse(1) { "<none>" }
            val shortId = (raw.Id ?: "").removePrefix("sha256:").take(12)
            val created = formatTimestamp(raw.Created ?: 0L)
            val size = formatBytes(raw.Size ?: 0L)

            Image(
                id = raw.Id ?: "",
                name = name,
                tag = tag,
                created = created,
                size = size
            )
        }
    }

    fun deleteImage(id: String) {
        dockerSocketAdapter.delete("/images/$id?force=false")
    }

    private fun formatTimestamp(epochSeconds: Long): String {
        return Instant.ofEpochSecond(epochSeconds)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    }

    private fun formatBytes(bytes: Long): String {
        return when {
            bytes >= 1_073_741_824 -> String.format("%.2f GB", bytes / 1_073_741_824.0)
            bytes >= 1_048_576 -> String.format("%.2f MB", bytes / 1_048_576.0)
            bytes >= 1_024 -> String.format("%.2f KB", bytes / 1_024.0)
            else -> "$bytes B"
        }
    }

    // --- Docker API raw models ---

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class DockerImage(
        val Id: String? = null,
        val RepoTags: List<String>? = null,
        val Created: Long? = null,
        val Size: Long? = null
    )
}
