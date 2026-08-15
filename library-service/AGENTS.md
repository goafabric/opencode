# AGENTS.md — library-service

Guidance for AI agents (and humans) working in `library-service/`.
This is a Quarkus **3.37.1** + **Kotlin 2.4.0** micro-service for the
**students – library – book** domain (JDK 25, Gradle 9.5.1).

> The canonical reference for every architectural decision is the example service in
> `../spec/quarkus/example.zip` (extract at `../spec/quarkus/example_extracted`).
> Match its patterns when extending this service.

## Build / run
Run these from the `library-service/` directory:

```bash
./gradlew clean build          # full build: compile + all @QuarkusTest + arch tests (green target)
./gradlew test                 # just the test task
./gradlew quarkusDev           # dev mode with live reload
./gradlew bootRun              # run the jvm jar
./gradlew dockerImageNative    # native image build + push (needs Docker + GraalVM)
```

- `gradlew clean build` must stay green. 3 native `@QuarkusIntegrationTest` cases
   (`native/LibraryControllerNativeIT`) auto-**skip** when Docker is unavailable; that is
   expected and not a failure.
- The build uses the `release` plugin, so do **not** add version tags manually.
- `quarkus.jacoco.report` and the "Mixing REST server / RESTEasy classic client" notices are
   pre-existing, harmless (inherited from the example spec).

## Domain model
- `Library` **holds** `Book`s (`OneToMany` cascade, owning side joined via `book.library_id`).
- A `Book` belongs to a `Library` and may be **lent to at most one `Student`**
   (`book.studentId`, nullable) — so a student can lend one or many books.
- Relations + demo data live in `persistence/DemoDataImporter.kt` and migration
   `resources/db/migration/V2__library.sql` (keep DDL and entities in sync — a missing
   column such as `library_id` breaks the whole app boot).

## Layered architecture (packages under `org.goafabric.librarieservice`)
```
controller          REST resources; each @Tool must have a unique name across the whole app
controller/dto      transfer types + *Search beans (no Dto/DTO/Result/Response suffix, enforced by ArchUnit)
logic               @Transactional @ApplicationScoped; the only layer that may touch the database
logic/mapper        MapStruct mappers (entity <-> dto); pure, must NOT touch repos/logic (ArchUnit)
adapter             outbound REST clients (@RegisterRestClient + @CircuitBreaker, suffix *Adapter)
extensions          HttpInterceptor / KafkaInterceptor / UserContext / ExceptionHandler /
                   ConfigTreeSourceFactory — copy VERBATIM, only change the base package
persistence         PanacheRepository.Managed + jakarta.data @Query; DemoDataImporter (adapt per domain)
persistence/entity  @Entity with @EntityListeners(AuditTrailListener::class, KafkaPublisher::class)
persistence/extensions  TenantResolver / AuditTrailListener / KafkaPublisher — copy VERBATIM
                      (KafkaPublisher must be adapted to the entity/topic list)
```
Architecture rules are enforced by `src/test/.../architecture/*.kt` (ArchUnit) — do not
introduce `com.google.common`/`org.apache.commons`, `Impl`/`Management` suffixes, or
Flyway Java migrations.

## Conventions that already bit us (keep in mind)
1. **Kotlin multi-line statements**: a line ending in `)`/`]`/`}` does **not** continue to the
   next line unless the next line starts with an operator (`.`, `+`, …). Put method-call
   continuations on a `.`-prefixed line, or keep `assertThat(...).isEqualTo(...)` on one line.
2. **Parens in demo/import code**: keep `logic.save(DTO(...))` blocks balanced; an unbalanced
   `)` breaks kapt codegen with a cryptic syntax error.
3. **MCP `@Tool` names are global** across all controllers — name each `getById`/`find`/`save`
   uniquely (e.g. `getStudentById`, `getLibraryById`, `lendBook`) or Quarkus fails to boot.
4. **MapStruct nested collection**: for a parent with a child collection, declare only the
   **single-item** child mapping in the parent mapper (MapStruct derives the list mapping).
   Declaring both `List<X>` and `Iterable<X>` causes an "ambiguous mapping" error.
5. **Validation**: a `@Size(min = 3)` field rejects 2-char input but accepts 3-char input —
   size your fixture data accordingly.
6. **Multi-tenancy is load-bearing**: `quarkus.hibernate-orm.multitenant=SCHEMA` +
   `TenantResolver` (schema prefix `library_`, tenants `0,5`) + `database.provisioning.goals
   =-migrate -import-demo-data`. The `TenantResolver` runs the Flyway migrations per schema at
   startup; do not set `quarkus.flyway.migrate-at-start=true`.
7. **Kafka publishing is disabled by default** (`mp.messaging.outgoing.general.enabled=false`)
   so no consumer/topic wiring is needed for a green build; `KafkaPublisher` only publishes when
   enabled.

## Tests
- `@QuarkusTest` integration tests cover the happy path + an update/delete (and clean up after
   themselves so demo-data counts stay stable).
- Unit tests use Mockito (`@InjectMock`) where a collaborator must be stubbed.
- DTO validation tests use the `jakarta.validation` `Validator` directly.
- Do **not** enable kafka/consumers for JVM tests unless you add a matching `@Incoming` consumer.

## Resume point
See `../PLAN.md`; continue at the first unchecked `[ ]` item.
