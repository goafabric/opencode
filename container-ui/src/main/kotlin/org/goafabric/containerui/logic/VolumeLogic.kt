package org.goafabric.containerui.logic

import jakarta.enterprise.context.ApplicationScoped
import org.goafabric.containerui.adapter.AppleContainerAdapter
import org.goafabric.containerui.adapter.model.RawVolume
import org.goafabric.containerui.controller.dto.Volume
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@ApplicationScoped
class VolumeLogic(private val adapter: AppleContainerAdapter) {

    fun listVolumes(): List<Volume> {
        val json = adapter.run("volume", "list", "--format", "json")
        val raw = adapter.parseList(json, RawVolume::class.java)

        return raw.map { vol ->
            Volume(
                name = vol.configuration?.name ?: vol.id ?: "",
                created = formatTimestamp(vol.configuration?.creationDate),
                size = formatBytes(vol.configuration?.sizeInBytes ?: 0L)
            )
        }
    }

    fun deleteVolume(name: String) { adapter.run("volume", "delete", name) }

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
