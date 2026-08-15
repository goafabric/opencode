package org.goafabric.librarieservice.persistence.extensions

import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Instance
import jakarta.persistence.PostPersist
import jakarta.persistence.PostRemove
import jakarta.persistence.PostUpdate
import jakarta.transaction.Synchronization
import jakarta.transaction.TransactionManager
import org.apache.kafka.common.header.internals.RecordHeaders
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.eclipse.microprofile.reactive.messaging.Message
import org.goafabric.librarieservice.extensions.UserContext
import org.goafabric.librarieservice.logic.mapper.BookMapper
import org.goafabric.librarieservice.logic.mapper.LibraryMapper
import org.goafabric.librarieservice.logic.mapper.StudentMapper
import org.goafabric.librarieservice.persistence.entity.BookEo
import org.goafabric.librarieservice.persistence.entity.LibraryEo
import org.goafabric.librarieservice.persistence.entity.StudentEo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets


@ApplicationScoped
class KafkaPublisher(
        @param:ConfigProperty(name = "mp.messaging.outgoing.general.enabled") private val kafkaEnabled: Boolean,
        @param:Channel("general") private val generalEmitter: Instance<Emitter<Any>>,
    private val transactionManager: TransactionManager,
    private val studentMapper: StudentMapper,
    private val bookMapper: BookMapper,
    private val libraryMapper: LibraryMapper
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
        if (!kafkaEnabled) return

        when (entity) {
            is StudentEo   -> publish("student", entity.id!!, operation, studentMapper.map(entity))
            is LibraryEo   -> publish("library", entity.id!!, operation, libraryMapper.map(entity))
            is BookEo      -> publish("book", entity.id!!, operation, bookMapper.map(entity))
            else -> error("Type " + entity::class)
            }
        }

    //publish both entities with the same topic channel to retain order, put Operation and UserContext to Kafka Headers to prevent EventData Wrapper
    private fun publish(topic: String, key: String, operation: DbOperation, payload: Any) {
        log.info("publishing event of type {}", topic)

        val headers = RecordHeaders()
        headers.add("operation", operation.toString().toByteArray(StandardCharsets.UTF_8))

        UserContext.adapterHeaderMap.forEach { (headerKey, value) ->
            headers.add(headerKey, value.toByteArray(StandardCharsets.UTF_8))
            }

        val metadata = OutgoingKafkaRecordMetadata.builder<String>()
              .withTopic(topic) // optional if already configured
              .withKey(key)
              .withHeaders(headers)
              .build()

        transactionManager.transaction.registerSynchronization(object : Synchronization {
            override fun beforeCompletion() {
                 //not implemented
              }

            override fun afterCompletion(status: Int) {
                if (status == jakarta.transaction.Status.STATUS_COMMITTED)    {
                    generalEmitter.get().send(Message.of(payload).addMetadata(metadata))
                }
              }
            })

        }

}
