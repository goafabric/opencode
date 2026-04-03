package org.goafabric.libraryservice.persistence.extensions

import jakarta.annotation.PostConstruct
import jakarta.persistence.PostPersist
import jakarta.persistence.PostRemove
import jakarta.persistence.PostUpdate
import org.apache.kafka.clients.producer.ProducerRecord
import org.goafabric.libraryservice.extensions.UserContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.function.BiConsumer

@Component
class KafkaPublisher(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    @Value("\${spring.kafka.enabled:false}") private val kafkaEnabled: Boolean
) {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    private fun publish(topic: String, key: String, payload: Any) {
        if (!kafkaEnabled) {
            return
        }

        log.info("publishing event to topic ${topic}/event/${key}")
        val producerRecord = ProducerRecord(topic, key, payload)
        producerRecord.headers().add("operation", "EVENT".toByteArray(StandardCharsets.UTF_8))
        producerRecord.headers().add("userId", userName.toByteArray(StandardCharsets.UTF_8))
        producerRecord.headers().add("tenantId", tenantId.toByteArray(StandardCharsets.UTF_8))
        producerRecord.headers().add("organizationId", organizationId.toByteArray(StandardCharsets.UTF_8))

        kafkaTemplate.send(producerRecord)
    }

    @PostConstruct
    fun init() {
        if (!kafkaEnabled) {
            log.warn("Kafka is disabled, KafkaPublisher will not publish any events")
        }
    }

    private val userName: String
        get() = UserContext.userName

    private val tenantId: String
        get() = UserContext.tenantId

    private val organizationId: String
        get() = UserContext.organizationId
}