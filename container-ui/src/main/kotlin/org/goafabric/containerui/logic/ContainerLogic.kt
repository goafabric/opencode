package org.goafabric.containerui.logic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.enterprise.context.ApplicationScoped
import org.goafabric.containerui.adapter.AppleContainerAdapter
import org.goafabric.containerui.controller.dto.Container
import org.goafabric.containerui.controller.dto.ContainerLog
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@ApplicationScoped
class ContainerLogic(private val adapter: AppleContainerAdapter) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    fun listContainers(): List<Container> {
        val json = adapter.run("list", "--all", "--format", "json")
        val raw = adapter.parseList(json, RawContainer::class.java)

        // Fetch stats for all running containers in one call
        val statsMap = fetchStatsMap()

        return raw.map { c ->
            val stats = statsMap[c.id]
            Container(
                id = c.id ?: "",
                name = c.id ?: "",
                image = c.configuration?.image?.reference ?: "",
                status = c.status?.state ?: "",
                state = c.status?.state ?: "",
                ports = formatPorts(c.configuration?.publishedPorts),
                cpuPercent = stats?.let { formatCpu(it.cpuDeltaUsec) } ?: "–",
                memoryUsage = stats?.let { formatMemory(it.memoryUsageBytes, it.memoryLimitBytes) } ?: "–"
            )
        }
    }

    fun startContainer(id: String) {
        adapter.run("start", id)
    }

    fun stopContainer(id: String) {
        adapter.run("stop", id)
    }

    fun deleteContainer(id: String) {
        adapter.run("delete", "--force", id)
    }

    fun getLogs(id: String): ContainerLog {
        val output = adapter.run("logs", "-n", "500", id)
        return ContainerLog(containerId = id, logs = output)
    }

    private fun fetchStatsMap(): Map<String, RawStats> {
        return try {
            val json1 = adapter.run("stats", "--no-stream", "--format", "json")
            val sample1 = adapter.parseList(json1, RawStats::class.java).associateBy { it.id ?: "" }
            Thread.sleep(1000)
            val json2 = adapter.run("stats", "--no-stream", "--format", "json")
            val sample2 = adapter.parseList(json2, RawStats::class.java)

            // Attach the delta to each second sample so formatCpu can use it
            sample2.map { s2 ->
                val prev = sample1[s2.id]?.cpuUsageUsec ?: s2.cpuUsageUsec ?: 0L
                val delta = (s2.cpuUsageUsec ?: 0L) - prev
                s2.copy(cpuDeltaUsec = delta)
            }.associateBy { it.id ?: "" }
        } catch (e: Exception) {
            log.debug("Could not fetch stats: {}", e.message)
            emptyMap()
        }
    }

    private fun formatCpu(cpuDeltaUsec: Long?): String {
        if (cpuDeltaUsec == null) return "–"
        // delta over 1 second interval → percentage of one core
        val pct = cpuDeltaUsec / 10_000.0   // usec / 1_000_000 * 100 = usec / 10_000
        return String.format("%.2f%%", pct)
    }

    private fun formatMemory(usageBytes: Long?, limitBytes: Long?): String {
        val used = formatBytes(usageBytes ?: 0L)
        val limit = formatBytes(limitBytes ?: 0L)
        return "$used / $limit"
    }

    private fun formatPorts(ports: List<RawPort>?): String {
        if (ports.isNullOrEmpty()) return ""
        return ports.joinToString(", ") {
            "${it.hostAddress ?: "0.0.0.0"}:${it.hostPort}→${it.containerPort}/${it.proto}"
        }
    }

    private fun formatBytes(bytes: Long): String {
        return when {
            bytes >= 1_073_741_824 -> String.format("%.1f GB", bytes / 1_073_741_824.0)
            bytes >= 1_048_576    -> String.format("%.1f MB", bytes / 1_048_576.0)
            bytes >= 1_024        -> String.format("%.1f KB", bytes / 1_024.0)
            else                  -> "$bytes B"
        }
    }

    // --- CLI JSON models ---

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class RawContainer(
        val id: String? = null,
        val configuration: RawConfig? = null,
        val status: RawStatus? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class RawConfig(
        val image: RawImage? = null,
        val publishedPorts: List<RawPort>? = null,
        val creationDate: String? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class RawImage(
        val reference: String? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class RawPort(
        val hostAddress: String? = null,
        val hostPort: Int? = null,
        val containerPort: Int? = null,
        val proto: String? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class RawStatus(
        val state: String? = null,
        val startedDate: String? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class RawStats(
        val id: String? = null,
        val cpuUsageUsec: Long? = null,
        val memoryUsageBytes: Long? = null,
        val memoryLimitBytes: Long? = null,
        val cpuDeltaUsec: Long? = null   // computed, not from JSON
    )
}
