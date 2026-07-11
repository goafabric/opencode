package org.goafabric.containerui.adapter

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.enterprise.context.ApplicationScoped
import org.slf4j.LoggerFactory

/**
 * Adapter for Apple Containers via the `container` CLI.
 * Shells out to `container <subcommand> --format json` and parses the result.
 */
@ApplicationScoped
class AppleContainerAdapter {
    private val log = LoggerFactory.getLogger(this.javaClass)

    val mapper: ObjectMapper = ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun run(vararg args: String): String {
        val cmd = listOf("container") + args.toList()
        log.debug("Running: {}", cmd.joinToString(" "))
        val proc = ProcessBuilder(cmd)
            .redirectErrorStream(true)
            .start()
        val output = proc.inputStream.bufferedReader().readText()
        val exitCode = proc.waitFor()
        if (exitCode != 0) {
            // Some commands (stop on already-stopped, delete, etc.) return non-zero but are harmless
            log.debug("container {} exited {}: {}", args.firstOrNull(), exitCode, output.trim())
        }
        return output
    }

    fun <T> parseList(json: String, type: Class<T>): List<T> {
        return mapper.readValue(json, mapper.typeFactory.constructCollectionType(List::class.java, type))
    }

    fun <T> parse(json: String, type: Class<T>): T {
        return mapper.readValue(json, type)
    }
}
