package org.goafabric.containerui.controller.dto

data class ContainerStats(
    val containerId: String,
    val cpuPercent: String,
    val memoryUsage: String
)
