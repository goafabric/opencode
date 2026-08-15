package org.goafabric.librarieservice.extensions

import io.quarkus.runtime.annotations.StaticInitSafe
import io.smallrye.config.ConfigSourceContext
import io.smallrye.config.ConfigSourceFactory
import org.eclipse.microprofile.config.spi.ConfigSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import java.util.*

//https://quarkus.io/guides/config-extending-support#custom-config-source
class ConfigTreeSourceFactory : ConfigSourceFactory {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun getConfigSources(context: ConfigSourceContext): Iterable<ConfigSource> {
        return getConfig(File(context.getValue("quarkus.configtree.path") ?.value ?: ""))
      }

    fun getConfig(directory: File): Iterable<ConfigSource> {
        return if (directory.name != "" && directory.exists() && directory.isDirectory) listOf(ConfigTreeSource(directory)) else {
            log.info("quarkus.configtree.path not found")
            emptyList()
          }
     }

    override fun getPriority(): OptionalInt = OptionalInt.of(290)

      @StaticInitSafe
    class ConfigTreeSource(private val root: File) : ConfigSource {

        private val properties: Map<String, String> = load()

        private fun load(): Map<String, String> {
            val map = mutableMapOf<String, String>()

            root.walkTopDown()
                 .filter { it.isFile }
                 .forEach { file ->
                     val relative = root.toPath().relativize(file.toPath()).toString()
                     val key = relative.replace(File.separatorChar, '.').replace(Regex("\\.+"), ".")
                     val value = Files.readString(file.toPath()).trim()
                     map[key] = value
                 }

            return map
          }

        override fun getProperties(): MutableMap<String, String> = properties.toMutableMap()
        override fun getPropertyNames(): MutableSet<String> = properties.keys.toMutableSet()
        override fun getValue(propertyName: String?): String? = properties[propertyName]
        override fun getName(): String = "configtree:${root.absolutePath}"
        override fun getOrdinal(): Int = 350
      }
}
