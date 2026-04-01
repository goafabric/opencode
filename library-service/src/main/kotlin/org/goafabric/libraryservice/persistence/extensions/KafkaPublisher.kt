package org.goafabric.libraryservice.persistence.extensions

import jakarta.persistence.PostPersist
import jakarta.persistence.PostRemove
import jakarta.persistence.PostUpdate
import org.apache.kafka.clients.producer.ProducerRecord
import org.goafabric.libraryservice.extensions.UserContext
import org.goafabric.libraryservice.logic.BookLendingMapper
import org.goafabric.libraryservice.logic.BookMapper
import org.goafabric.libraryservice.logic.LibraryMapper
import org.goafabric.libraryservice.logic.StudentMapper
import org.goafabric.libraryservice.persistence.entity.BookEo
import org.goafabric.libraryservice.persistence.entity.BookLendingEo
import org.goafabric.libraryservice.persistence.entity.LibraryEo
import org.goafabric.libraryservice.persistence.entity.StudentEo
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
    @param:Value("\${spring.kafka.enabled:false}") private val kafkaEnabled: Boolean,
    private val libraryMapper: LibraryMapper,
    private val bookMapper: BookMapper,
    private val studentMapper: StudentMapper,
    private val bookLendingMapper: BookLendingMapper
) {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    private enum class DbOperation {  CREATE, UPDATE, DELETE }

    @PostPersist
    fun afterCreate(`object`: Any) {
        publish(DbOperation.CREATE, `object`)
    }

    @PostUpdate
    fun afterUpdate(`object`: Any) {
        publish(DbOperation.UPDATE, `object`)
    }

    @PostRemove
    fun afterDelete(`object`: Any) {
        publish(DbOperation.DELETE, `object`)
    }

    private fun publish(operation: DbOperation, entity: Any) {
        if (!kafkaEnabled) {
            return
        }

        when (entity) {
            is LibraryEo      -> publish("library", entity.id!!, operation, libraryMapper.map(entity))
            is BookEo          -> publish("book", entity.id!!, operation, bookMapper.map(entity))
            is StudentEo       -> publish("student", entity.id!!, operation, studentMapper.map(entity))
            is BookLendingEo   -> publish("book-lending", entity.id!!, operation, bookLendingMapper.map(entity))
            else -> error("Type " + entity::class)
        }
    }

    private fun publish(topic: String, key: String, operation: DbOperation, payload: Any) {
        log.info("publishing event of type {}", topic)
        val producerRecord = ProducerRecord(topic, key, payload)
        producerRecord.headers().add("operation", operation.toString().toByteArray(StandardCharsets.UTF_8))

        UserContext.adapterHeaderMap.forEach(BiConsumer { key1: String, value: String ->
            producerRecord.headers().add(
                key1, value.toByteArray(StandardCharsets.UTF_8)
            )
        })

        kafkaTemplate.send(producerRecord)
    }
}
