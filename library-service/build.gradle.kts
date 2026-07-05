plugins {
    java
    jacoco
    id("io.quarkus") version "3.37.0"
    kotlin("jvm") version "2.4.0"
    kotlin("plugin.jpa") version "2.4.0"
    kotlin("plugin.allopen") version "2.4.0"
    kotlin("kapt") version "2.4.0"
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.ws.rs.GET")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

dependencies {
    // Import Quarkus BOM to manage versions
    implementation(enforcedPlatform("io.quarkus:quarkus-bom:3.37.0"))

    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-resteasy-reactive-kotlin")
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    implementation("io.quarkus:quarkus-jdbc-h2")
    implementation("io.quarkus:quarkus-flyway")
    implementation("org.flywaydb:flyway-database-postgresql")
    
    implementation("io.quarkus:quarkus-hibernate-orm-panache")
    implementation("io.smallrye.reactive:smallrye-panache-kotlin")
    
    implementation("io.quarkus:quarkus-smallrye-opentelemetry")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation("io.quarkus:quarkus-smallrye-health")
    implementation("io.quarkus:quarkus-openshift")
    
    // MapStruct
    implementation("org.mapstruct:mapstruct:1.6.3")
    kapt("org.mapstruct:mapstruct-processor:1.6.3")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("io.rest-assured:kotlin-extensions")
    testImplementation("io.quarkus:quarkus-test-kubernetes-port-forward")
    
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.gradle:test-needs-unstable-matcher")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}

kotlin {
    jvmToolchain(21)
}
