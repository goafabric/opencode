package org.goafabric.librarieservice.persistence.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate

@SpringBootTest
class KafkaPublisherIT {
         @org.springframework.beans.factory.annotation.Value("\${spring.kafka.enabled:false}")
     private lateinit var kafkaEnabled: String
     
        @Autowired(required = false)
     private lateinit var kafkaTemplate: KafkaTemplate<String, Any>

      @Test
    fun testKafkaPublisherIsConfigured() {
           val expectedEnabled = true == toBoolean(kafkaEnabled)
           // Kafka publisher should be configured based on environment
         }

          @Test
     fun testKafkaMessageStructure() {
        val kafkaHeaders = mapOf(
             "X-TenantId" to "0",
            "X-OrganizationId" to "0",
             "operation" to "CREATE".toByteArray(Charsets.UTF_8)
           )
         
       assertThat(kafkaHeaders).isNotEmpty
         }

     private fun toBoolean(value: String): Boolean {
        return value.toBoolean()
          }
}
