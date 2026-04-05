# Technical Specification

---

## 1. Purpose & Scope

This project serves as a **microservice template** within the *eden* platform, demonstrating how to build production-ready microservices. It is based on these core technologies:

- **Spring Boot 4.0.x** as the underlying framework
- **Kotlin** as the primary language (JDK 25)
- **Gradle (Kotlin DSL)** (`build.gradle.kts`) for the build process
- **PostgreSQL** for production persistence, **H2** for development/test
- **Apache Kafka** for event-driven messaging
- **Gitignore File** (`.gitignore`) to specify which files should not be added to GIT

This specification defines the technical blueprint covering the layered architecture, cross-cutting concerns (multi-tenancy, authorization, audit trail, event publishing), non-functional quality attributes, infrastructure, and enforced architecture rules. It deliberately excludes any business or domain-specific requirements.

---

## 2. Technology Stack

| Concern              | Technology / Version                                      |
|----------------------|-----------------------------------------------------------|
| Language             | Kotlin 2.3.x (JDK 25)                                    |
| Framework            | Spring Boot 4.0.x                                        |
| Build                | Gradle (Kotlin DSL)                                      |
| Persistence          | Spring Data JPA, Hibernate 7, Flyway                     |
| Database             | PostgreSQL 16 (production), H2 (development/test)        |
| Messaging            | Apache Kafka (spring-kafka)                               |
| HTTP Client          | Spring Declarative REST Client (`@HttpExchange`)          |
| Resilience           | Resilience4j Circuit Breaker                              |
| Mapping              | MapStruct (via `kapt`)                                    |
| API Documentation    | SpringDoc OpenAPI (Swagger UI)                            |
| Observability        | Micrometer, Prometheus, OpenTelemetry (traces)            |
| Container Build      | Jib (JVM image), Spring Boot Buildpacks (native image)    |
| Native Compilation   | GraalVM Native Image                                      |
| Code Quality         | ArchUnit, JaCoCo, SonarQube                               |
| SBOM / Compliance    | ECS Gradle Plugin (TrustSource)                           |
| Service Catalog      | Backstage (`catalog-info.yaml`)                           |

---

## 3. Architecture

### 3.1 Layered Architecture

The service follows a strict **Controller → Logic → Persistence** layered architecture, enforced at compile-test time via ArchUnit.

```
┌──────────────────────────────────────────────────────────────┐
│  Controller Layer  (REST API, DTOs, Validation)              │
│  ├── *Controller  (@RestController)                          │
│  └── dto/         (Kotlin data classes)                      │
├──────────────────────────────────────────────────────────────┤
│  Logic Layer       (Business logic, Transaction boundary)    │
│  ├── *Logic       (@Component, @Transactional)               │
│  └── mapper/      (MapStruct interfaces)                     │
├──────────────────────────────────────────────────────────────┤
│  Persistence Layer (Repositories, Entities, Extensions)      │
│  ├── *Repository  (Spring Data JPA)                          │
│  ├── entity/      (*Eo classes, JPA entities)                │
│  └── extensions/  (AuditTrail, Kafka, TenantResolver)        │
├──────────────────────────────────────────────────────────────┤
│  Adapter Layer     (Outbound HTTP calls)                     │
│  └── *Adapter     (@HttpExchange, @CircuitBreaker)           │
├──────────────────────────────────────────────────────────────┤
│  Extensions        (Cross-cutting concerns)                  │
│  ├── ExceptionHandler      (@ControllerAdvice)               │
│  ├── HttpInterceptor       (HandlerInterceptor)              │
│  ├── KafkaInterceptor      (RecordInterceptor)               │
│  └── authorization/        (JWT, Permissions, AOP)           │
└──────────────────────────────────────────────────────────────┘
```

**Layer dependency rules** (enforced by ArchUnit):

| Rule | Description |
|------|-------------|
| Controller → Logic | Controllers depend on Logic only. No other layer may access Controllers. |
| Logic → Persistence | Logic layer may access Persistence. Only Controllers may access Logic. |
| Persistence is internal | Only Logic may access the Persistence layer. |

### 3.2 Package Conventions

| Package            | Purpose                                        | Naming Convention          |
|--------------------|------------------------------------------------|----------------------------|
| `controller`       | REST endpoints                                 | `*Controller`              |
| `controller.dto`   | Request/response data                          | Kotlin `data class`        |
| `logic`            | Orchestration, authorization, transactions     | `*Logic`                   |
| `logic.mapper`     | DTO ↔ Entity mapping                           | `*Mapper` (MapStruct)      |
| `persistence`      | Spring Data repositories                       | `*Repository`              |
| `persistence.entity` | JPA entities                                 | `*Eo`                      |
| `persistence.extensions` | Audit trail, Kafka publisher, tenant resolver | Descriptive names      |
| `adapter`          | Outbound HTTP service clients                  | `*Adapter`                 |
| `extensions`       | Cross-cutting interceptors, exception handling | Descriptive names          |
| `extensions.authorization` | AuthN/AuthZ components                 | Descriptive names          |

### 3.3 Architecture Rules (ArchUnit-Enforced)

The following architectural constraints are automatically validated during the test phase:

**Controller rules:**
- Classes annotated `@RestController` must end with `Controller`.
- Controllers must not use `@CrossOrigin` (CORS is handled centrally via `WebMvcConfigurer`).
- Controller methods must not return `ResponseEntity` (return domain objects directly; status mapping via `@ControllerAdvice`).
- DTOs should not be suffixed with "Request"
- DTOs should not be suffixed with "Response"

**Adapter rules:**
- Methods annotated with `@HttpExchange` must be declared in classes ending with `Adapter`.
- Direct usage of `RestClient`, `RestTemplate`, or `WebClient` is forbidden outside configuration classes — only declarative REST clients are allowed (required for native image support).
- Adapter interfaces must be annotated with `@CircuitBreaker`.

**Persistence rules:**
- Classes extending `Repository` must end with `Repository`.
- Logic classes using repositories must be annotated with `@Transactional`.

**Application-wide rules:**
- Reflection usage is forbidden outside `@Configuration` classes, `RuntimeHintsRegistrar` implementations, and entity listeners (required for native image support).
- Aspects annotated with `@Aspect` must declare `@RegisterReflection` or `@ImportRuntimeHints`.
- `@Async` is banned — it erases ThreadLocals, swallows exceptions, breaks Resilience4j, and causes concurrency issues.
- Flyway Java migrations are banned — complex import logic belongs in a separate batch.
- **Banned libraries**: Google Guava, Apache Commons — Java 21+ and Spring provide equivalent functionality.
- **Allowed library whitelist**: Only explicitly listed packages are permitted as dependencies (Spring, Hibernate, MapStruct, Resilience4j, Micrometer, Springdoc, OpenTelemetry, Kafka, Jackson, Flyway, Kotlin stdlib, etc.).
- Class names must not end with `Impl` or `Management` (naming anti-patterns).

### 3.4 New Project Guidance

When creating a new project from this template, the following guidance applies:

**Copy unmodified:**

- `extensions/` package — Exception handling, `HttpInterceptor`, `KafkaInterceptor`, and all authorization extensions.
- `persistence/extensions/` package — Audit trail (`AuditTrailListener`), Kafka publisher (`KafkaPublisher`), and tenant resolver.
- `architecture/` test package — All ArchUnit rules tests (`*RulesTest.kt`).

**Adapt to project requirements:**

- `controller/` package — Define project-specific REST endpoints and DTOs.
- `logic/` package — Implement project-specific business logic and MapStruct mappers.
- `persistence/` package — Define project-specific repositories, entities, and Flyway migration scripts (`resources/db/migration/`).
- `persistence/DemoDataImporter` — Adapt demo data generation to project-specific entities.
- `adapter/` package — Define project-specific outbound HTTP service clients.
- `resources/application.yml` — Contains property configurations for local development; adapt as needed.

**Entity listeners:** For audit trail and Kafka publishing to take effect, entities must be annotated with:

```text
@EntityListeners(AuditTrailListener::class, KafkaPublisher::class)
```

---

## 4. Cross-Cutting Concerns

### 4.1 Multi-Tenancy

- **Strategy**: Schema-per-tenant isolation using Hibernate's `MultiTenantConnectionProvider` and `CurrentTenantIdentifierResolver`.
- **Schema naming**: `{schema-prefix}{tenantId}` (e.g., `core_0`, `core_5`).
- **Tenant resolution**: Extracted from the JWT bearer token via `AccessContextUtil.getTenantId()`.
- **Organization discriminator**: Hibernate `@TenantId` annotation on the `organizationId` field for row-level filtering within a schema.
- **Flyway migration**: Schemas are provisioned at startup by iterating over the configured tenant list and running Flyway migrations per schema. Supports placeholders (`${tenantId}`) in SQL scripts.
- **Configuration**:
  - `multi-tenancy.tenants` — comma-separated list of tenant IDs.
  - `multi-tenancy.schema-prefix` — prefix for schema names (default: `_`).
  - `multi-tenancy.default-schema` — fallback schema (default: `PUBLIC`).

### 4.3 Audit Trail

- **Mechanism**: JPA `@EntityListeners` with `AuditTrailListener` attached to every entity.
- **Triggers**: `@PostPersist`, `@PostUpdate`, `@PostRemove` JPA lifecycle callbacks.
- **Captured data**: Organization ID, object type (entity class name without `Eo` suffix, lowercased), object ID, operation (CREATE/UPDATE/DELETE), created/modified by (user), created/modified at (timestamp), old value (JSON), new value (JSON).
- **Old-value retrieval**: Uses a separate `REQUIRES_NEW` transaction to read the pre-update state via `AuditDao.findOldObject()`. A deep copy via JSON serialization avoids lazy-loading issues.
- **Storage**: Dedicated `audit_trail` table per tenant schema, indexed on `organization_id`.
- **Error handling**: Audit failures are caught and logged without affecting the main transaction.

### 4.4 Event Publishing (Kafka)

- **Mechanism**: JPA `@EntityListeners` with `KafkaPublisher` attached to entities.
- **Triggers**: `@PostPersist`, `@PostUpdate`, `@PostRemove` — same lifecycle hooks as audit trail.
- **Enable/disable**: Controlled by `spring.kafka.enabled` property (default: `false`).
- **Message structure**: `ProducerRecord` with entity type as topic, entity ID as key, mapped DTO as JSON value. Operation type and bearer token passed as Kafka headers.
- **Entity-to-topic mapping**: The `KafkaPublisher` uses a `when` expression to map entity types to topics and uses `PersonMapper` for DTO conversion.
- **Inbound processing**: `KafkaInterceptor` provides a `ConcurrentKafkaListenerContainerFactory` bean with a `RecordInterceptor` that sets the access context from message headers.
- **Custom actuator endpoint**: `/actuator/topics` lists available Kafka topics.

### 4.5 Exception Handling

- **Centralized** via `@ControllerAdvice` (`ExceptionHandler`).
- **Mapping**:

| Exception                      | HTTP Status            |
|--------------------------------|------------------------|
| `IllegalArgumentException`     | 412 Precondition Failed |
| `IllegalStateException`        | 412 Precondition Failed |
| `ConstraintViolationException` | 412 Precondition Failed |
| `AccessDeniedException`        | 403 Forbidden          |
| `Exception` (catch-all)        | 400 Bad Request        |

### 4.6 CORS

- Configurable via `cors.enabled` property (default: `false`).
- When enabled, allows all origins and all methods via `WebMvcConfigurer.addCorsMappings`.
- `@CrossOrigin` on individual controllers is prohibited by architecture rules — CORS is handled centrally.

### 4.7 Logging & Tracing

- **Structured logging**: Tenant ID injected into MDC (`tenantId` key) and log pattern: `%5p tenantId=%X{tenantId:-}`.
- **Distributed tracing**: OpenTelemetry auto-instrumentation via `spring-boot-starter-opentelemetry`. Tenant ID added as span attribute `tenant.id`.
- **Sampling**: Configurable via `management.tracing.sampling.probability` (default: `1.0` = 100%).
- **Export**: OTLP endpoint for traces (default: `http://localhost:4318/v1/traces`).
- **Datasource tracing**: `datasource-micrometer-spring-boot` for SQL-level observability.
