plugins {
    id("org.springframework.boot") version("3.4.5")
    id("io.spring.dependency-management") version("1.1.7")
    kotlin("jvm") version("2.1.0")
    kotlin("plugin.spring") version("2.1.0")
    kotlin("plugin.jpa") version("2.1.0")
}

group = "org.goafabric"
version = "1.0.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.flywaydb:flyway-core")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")

    // resilience4j
    implementation("io.github.resilience4j:resilience4j-spring-boot3")

    // http client
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // annotation processor for mapstruct
    implementation("org.mapstruct:mapstruct")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    annotationProcessor("org.mapstruct:mapstruct-processor")
    annotationProcessor("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // OpenTelemetry
    implementation("dev.mayehrmann:gcp-otel-exporter:1.11.0")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp:1.49.0")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp-http:1.49.0")

    // spring-kafka
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter")

}

tasks {
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
            freeCompilerArgs += "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
            javaCompilerArgs.addAll(
                "-parameters",
                "-Xlint:deprecation",
                "-Xjvm-default=all"
            )
        }
    }
    compileTestKotlin {
        kotlinOptions {
            freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
            javaCompilerArgs.addAll(
                "-parameters",
                "-Xlint:deprecation"
            )
        }
    }
}

tasks.named("test") {
    useJUnitPlatform()
}
