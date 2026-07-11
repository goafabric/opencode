package org.goafabric.containerui.adapter

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.StandardProtocolFamily
import java.net.UnixDomainSocketAddress
import java.nio.channels.Channels
import java.nio.channels.SocketChannel

/**
 * Adapter for communicating with the Docker Engine API via Unix domain socket.
 * Supports DOCKER_HOST=unix:// style configuration.
 */
@ApplicationScoped
class DockerSocketAdapter(
    @ConfigProperty(name = "docker.host", defaultValue = "unix:///var/run/docker.sock")
    private val dockerHost: String
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    private val mapper: ObjectMapper = ObjectMapper()
        .registerKotlinModule()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    private fun socketPath(): String {
        // strip unix:// prefix
        return dockerHost.removePrefix("unix://").removePrefix("unix:")
    }

    /**
     * Perform a GET request against the Docker Engine API.
     */
    fun get(path: String): String {
        return execute("GET $path HTTP/1.0\r\nHost: localhost\r\n\r\n")
    }

    /**
     * Perform a POST request with no body.
     */
    fun post(path: String): String {
        return execute("POST $path HTTP/1.0\r\nHost: localhost\r\nContent-Length: 0\r\n\r\n")
    }

    /**
     * Perform a DELETE request.
     */
    fun delete(path: String): String {
        return execute("DELETE $path HTTP/1.0\r\nHost: localhost\r\n\r\n")
    }

    private fun execute(rawRequest: String): String {
        val socketFile = socketPath()
        log.debug("Docker socket request to {} : {}", socketFile, rawRequest.lines().first())

        try {
            SocketChannel.open(StandardProtocolFamily.UNIX).use { channel ->
                channel.connect(UnixDomainSocketAddress.of(socketFile))

                val writer = PrintWriter(Channels.newOutputStream(channel))
                writer.print(rawRequest)
                writer.flush()

                val reader = BufferedReader(InputStreamReader(Channels.newInputStream(channel)))
                val response = StringBuilder()
                var line: String?
                var headersDone = false
                val body = StringBuilder()

                while (reader.readLine().also { line = it } != null) {
                    val currentLine = line!!
                    if (!headersDone) {
                        if (currentLine.isEmpty()) {
                            headersDone = true
                        }
                        response.append(currentLine).append("\n")
                    } else {
                        body.append(currentLine).append("\n")
                    }
                }

                return body.toString().trim()
            }
        } catch (e: Exception) {
            log.error("Error communicating with Docker socket at {}: {}", socketFile, e.message)
            throw IllegalStateException("Cannot connect to Docker socket at $socketFile: ${e.message}", e)
        }
    }

    fun <T> parseList(json: String, elementType: Class<T>): List<T> {
        return mapper.readValue(json, mapper.typeFactory.constructCollectionType(List::class.java, elementType))
    }

    fun <T> parse(json: String, type: Class<T>): T {
        return mapper.readValue(json, type)
    }
}
