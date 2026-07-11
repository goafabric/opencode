package org.goafabric.containerui.logic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.enterprise.context.ApplicationScoped
import org.goafabric.containerui.adapter.DockerSocketAdapter
import org.goafabric.containerui.controller.dto.Container
import org.goafabric.containerui.controller.dto.ContainerLog
import org.slf4j.LoggerFactory

@ApplicationScoped
class ContainerLogic(private val dockerSocketAdapter: DockerSocketAdapter) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    fun listContainers(): List<Container> {
        val json = dockerSocketAdapter.get("/containers/json?all=true")
        val rawContainers = dockerSocketAdapter.parseList(json, DockerContainer::class.java)

        return rawContainers.map { raw ->
            Container(
                id = raw.Id ?: "",
                name = raw.Names?.firstOrNull()?.removePrefix("/") ?: "",
                image = raw.Image ?: "",
                status = raw.Status ?: "",
                state = raw.State ?: "",
                ports = formatPorts(raw.Ports),
                cpuPercent = "–",
                memoryUsage = "–"
            )
        }
    }



    fun startContainer(id: String) {
        dockerSocketAdapter.post("/containers/$id/start")
    }

    fun stopContainer(id: String) {
        dockerSocketAdapter.post("/containers/$id/stop")
    }

    fun deleteContainer(id: String) {
        dockerSocketAdapter.delete("/containers/$id?force=true")
    }

    fun getLogs(id: String): ContainerLog {
        val raw = dockerSocketAdapter.get("/containers/$id/logs?stdout=true&stderr=true&tail=500&timestamps=false")
        // Docker log stream has 8-byte header per line when using multiplexed stream; strip non-printable chars
        val cleaned = raw.lines()
            .map { line -> line.filter { it.code >= 32 || it == '\n' || it == '\t' } }
            .joinToString("\n")
        return ContainerLog(containerId = id, logs = cleaned)
    }



    private fun formatPorts(ports: List<DockerPort>?): String {
        if (ports.isNullOrEmpty()) return ""
        return ports
            .filter { it.PublicPort != null }
            .joinToString(", ") { "${it.IP ?: "0.0.0.0"}:${it.PublicPort}->${it.PrivatePort}/${it.Type}" }
            .ifEmpty {
                ports.joinToString(", ") { "${it.PrivatePort}/${it.Type}" }
            }
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
    data class DockerContainer(
        val Id: String? = null,
        val Names: List<String>? = null,
        val Image: String? = null,
        val Status: String? = null,
        val State: String? = null,
        val Ports: List<DockerPort> = emptyList()
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class DockerPort(
        val IP: String? = null,
        val PrivatePort: Int? = null,
        val PublicPort: Int? = null,
        val Type: String? = null
    )

}
