# library-service (quarkus + kotlin)

A supporting Quarkus/Kotlin micro-service for the **students – library – book** domain.

## Domain
- `Library` holds many `Book`s.
- A `Book` belongs to a `Library` and can be lent to at most one `Student`
   (`book.studentId`). A student can therefore lend one or many books.
- `Student` can lend one or many books.

## Run
   ./gradlew --info test
   ./gradlew bootRun            # dev mode
   ./gradlew quarkusBuild       # jvm jar / runnable
   ./gradlew dockerImageNative  # native container image

   ##docker compose
   go to /src/deploy/docker and do "./stack up"

## Architecture
   controller      rest controllers (+ mcp `@Tool`)
   controller/dto  transfer objects + search beans
   logic           business logic (`@Transactional`)
   logic/mapper    mapstruct mappers (entity <-> dto)
   adapter         outbound http adapters
   extensions      crosscutting http/kafka interceptors, exception handling, user context
   persistence     panache repositories (+ jakarta data), demo data import
   persistence/entity         jpa entities with audittrail + kafka publishing
   persistence/extensions     audittrail, kafka publisher, tenant resolver
