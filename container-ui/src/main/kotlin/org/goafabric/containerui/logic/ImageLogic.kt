package org.goafabric.containerui.logic

import jakarta.enterprise.context.ApplicationScoped
import org.goafabric.containerui.adapter.AppleContainerAdapter
import org.goafabric.containerui.adapter.model.RawImage
import org.goafabric.containerui.controller.dto.Image
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@ApplicationScoped
class ImageLogic(private val adapter: AppleContainerAdapter) {

    fun listImages(): List<Image> {
        val json = adapter.run("image", "list", "--format", "json")
        val raw = adapter.parseList(json, RawImage::class.java)

        return raw.map { img ->
            val fullName = img.configuration?.name ?: ""
            val lastColon = fullName.lastIndexOf(':')
            val name = if (lastColon > 0) fullName.substring(0, lastColon) else fullName
            val tag  = if (lastColon > 0) fullName.substring(lastColon + 1) else "latest"
            val size = img.variants?.sumOf { it.size ?: 0L } ?: 0L

            Image(
                id = img.id ?: "",
                name = name,
                tag = tag,
                created = formatTimestamp(img.configuration?.creationDate),
                size = formatBytes(size)
            )
        }
    }

    fun deleteImage(id: String) { adapter.run("image", "delete", id) }

    private fun formatTimestamp(iso: String?): String {
        if (iso.isNullOrBlank()) return "–"
        return try {
            OffsetDateTime.parse(iso).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        } catch (_: Exception) { iso }
    }

    private fun formatBytes(bytes: Long): String = when {
        bytes >= 1_073_741_824 -> String.format("%.1f GB", bytes / 1_073_741_824.0)
        bytes >= 1_048_576     -> String.format("%.1f MB", bytes / 1_048_576.0)
        bytes >= 1_024         -> String.format("%.1f KB", bytes / 1_024.0)
        else                   -> "$bytes B"
    }
}
