package org.goafabric.librarieservice.extensions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.opentelemetry.api.trace.Span
import io.opentelemetry.context.Context
import io.quarkus.jackson.ObjectMapperCustomizer
import jakarta.annotation.Priority
import jakarta.inject.Singleton
import jakarta.interceptor.AroundInvoke
import jakarta.interceptor.Interceptor
import jakarta.interceptor.InvocationContext
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.header.Headers
import org.slf4j.MDC
import java.nio.charset.StandardCharsets

@KafkaUserInterceptor
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
class KafkaInterceptor {

      @AroundInvoke
    fun listen(context: InvocationContext): Any? {
        val consumerRecord = context.parameters[0] as ConsumerRecord<*, *>
        setContext(consumerRecord.headers())
        return context.proceed()
      }

    private fun setContext(headers: Headers) {
        UserContext.setContext(
            getValue(headers, "X-TenantId"),
            getValue(headers, "X-OrganizationId"),
            getValue(headers, "X-Auth-Request-Preferred-Username"), null
          )
        configureLogsAndTracing()
      }

    private fun configureLogsAndTracing() {
        MDC.put("tenantId", UserContext.tenantId)
        Span.fromContext(Context.current()).setAttribute("tenant.id", UserContext.tenantId)
      }


    companion object {
        fun getOperation(consumerRecord: ConsumerRecord<*,*>): String {
            return getValue(consumerRecord.headers(), "operation")
          }

        private fun getValue(headers: Headers, key: String): String {
            return String(headers.lastHeader(key).value(), StandardCharsets.UTF_8)
          }

      }
}

// extension function for get operation
fun ConsumerRecord<*,*>.operation(): String {
    return KafkaInterceptor.getOperation(this)
}

//required for Kafka Deserializer to work with Data Classes + Kafka Consumer
@Singleton
class KotlinModuleCustomizer : ObjectMapperCustomizer {
    override fun customize(objectMapper: ObjectMapper) {
        objectMapper.registerModule(KotlinModule.Builder().build())
      }
}
