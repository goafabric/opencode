package org.goafabric.librarieservice.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

class ConfigTreeSourceFactoryTest {

      @TempDir
    lateinit var tempDir: Path

      @Test
    fun `should load config tree from nested directories`() {
          // given
        val secretsDir = tempDir.resolve("secrets/spring/datasource")
        Files.createDirectories(secretsDir)

        val usernameFile = secretsDir.resolve("username")
        Files.writeString(usernameFile, "library-service")

        val factory = ConfigTreeSourceFactory()

          // when
        val sources = factory.getConfig(tempDir.resolve("secrets").toFile())
        val configSource = sources.first()

          // then
        val value = configSource.getValue("spring.datasource.username")
        assertThat(value).isEqualTo("library-service")
           }
}
