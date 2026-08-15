package org.goafabric.librarieservice.extensions

import jakarta.interceptor.InterceptorBinding
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FUNCTION

@InterceptorBinding
@Retention(RUNTIME)
@Target(CLASS, FUNCTION)
annotation class KafkaUserInterceptor
