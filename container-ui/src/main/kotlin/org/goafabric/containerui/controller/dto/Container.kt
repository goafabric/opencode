package org.goafabric.containerui.controller.dto

data class Container(
    val id: String,
    val name: String,
    val image: String,
    val status: String,
    val state: String,
    val ports: String,
    val cpuPercent: String,
    val memoryUsage: String
)
