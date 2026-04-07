package org.goafabric.librarieservice.persistence.extensions

import jakarta.persistence.PostPersist
import jakarta.persistence.PostRemove
import jakarta.persistence.PostUpdate
import org.apache.kafka.clients.producer.ProducerRecord
import org.goafabric.librarieservice.extensions.UserContext
import org.goafabric.librarieservice.logic.BookMapper
import org.goafabric.librarieservice.logic.LoanMapper
import org.goafabric.librarieservice.logic.StudentMapper
import org.goafabric.librarieservice.persistence.entity.BookEo
import org.goafabric.librarieservice.persistence.entity.LoanEo
import org.goafabric.librarieservice.persistence.entity.StudentEo
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
    private val studentMapper: StudentMapper,
    private val bookMapper: BookMapper,
    private val loanMapper: LoanMapper
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
            is StudentEo   -> publish("student", entity.id!!, operation, studentMapper.map(entity))
            is BookEo      -> publish("book", entity.id!!, operation, bookMapper.map(entity))
            is LoanEo      -> publish("loan", entity.id!!, operation, loanMapper.map(entity))
            else -> error("Type " + entity::class)
          }
       }

       //publish both person and address with the same topic to retain order, put Operation and UserContext to Kafka Headers to prevent EventData Wrapper
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
