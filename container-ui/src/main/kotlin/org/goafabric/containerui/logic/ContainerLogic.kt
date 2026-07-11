package org.goafabric.containerui.logic

import jakarta.enterprise.context.ApplicationScoped
import org.goafabric.containerui.adapter.AppleContainerAdapter
import org.goafabric.containerui.adapter.model.RawContainer
import org.goafabric.containerui.adapter.model.RawStats
import org.goafabric.containerui.controller.dto.Container
import org.goafabric.containerui.controller.dto.ContainerLog
import org.goafabric.containerui.controller.dto.ContainerStats
import org.slf4j.LoggerFactory

@ApplicationScoped
class ContainerLogic(private val adapter: AppleContainerAdapter) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    fun listContainers(): List<Container> {
        val json = adapter.run("list", "--all", "--format", "json")
        val raw = adapter.parseList(json, RawContainer::class.java)

        return raw.map { c ->
            Container(
                id = c.id ?: "",
                name = c.id ?: "",
                image = c.configuration?.image?.reference ?: "",
                status = c.status?.state ?: "",
                state = c.status?.state ?: "",
                ports = formatPorts(c),
                cpuPercent = "–",
                memoryUsage = "–"
            )
        }
    }

    fun fetchStats(): List<ContainerStats> {
        return try {
            val json1 = adapter.run("stats", "--no-stream", "--format", "json")
            val sample1 = adapter.parseList(json1, RawStats::class.java).associateBy { it.id ?: "" }
            Thread.sleep(1000)
            val json2 = adapter.run("stats", "--no-stream", "--format", "json")
            val sample2 = adapter.parseList(json2, RawStats::class.java)

            sample2.map { s2 ->
                val deltaUsec = (s2.cpuUsageUsec ?: 0L) - (sample1[s2.id]?.cpuUsageUsec ?: s2.cpuUsageUsec ?: 0L)
                ContainerStats(
                    containerId = s2.id ?: "",
                    cpuPercent = formatCpu(deltaUsec),
                    memoryUsage = formatMemory(s2.memoryUsageBytes, s2.memoryLimitBytes)
                )
            }
        } catch (e: Exception) {
            log.debug("Could not fetch stats: {}", e.message)
            emptyList()
        }
    }

    fun startContainer(id: String) { adapter.run("start", id) }
    fun stopContainer(id: String)  { adapter.run("stop", id) }
    fun deleteContainer(id: String) { adapter.run("delete", "--force", id) }

    fun getLogs(id: String): ContainerLog {
        val output = adapter.run("logs", "-n", "500", id)
        return ContainerLog(containerId = id, logs = output)
    }

    private fun formatCpu(deltaUsec: Long): String {
        val pct = deltaUsec / 10_000.0
        return String.format("%.2f%%", pct)
    }

    private fun formatMemory(usageBytes: Long?, limitBytes: Long?): String {
        return "${formatBytes(usageBytes ?: 0L)} / ${formatBytes(limitBytes ?: 0L)}"
    }

    private fun formatPorts(c: RawContainer): String {
        val ports = c.configuration?.publishedPorts ?: return ""
        if (ports.isEmpty()) return ""
        return ports.joinToString(", ") {
            "${it.hostAddress ?: "0.0.0.0"}:${it.hostPort}→${it.containerPort}/${it.proto}"
        }
    }

    private fun formatBytes(bytes: Long): String = when {
        bytes >= 1_073_741_824 -> String.format("%.1f GB", bytes / 1_073_741_824.0)
        bytes >= 1_048_576     -> String.format("%.1f MB", bytes / 1_048_576.0)
        bytes >= 1_024         -> String.format("%.1f KB", bytes / 1_024.0)
        else                   -> "$bytes B"
    }
}
