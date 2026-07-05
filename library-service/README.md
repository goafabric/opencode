# Library Service

A Quarkus-based Kotlin service for managing a library (Students, Books, Loans).

## Features
- Students and Books entity management via JPA.
- Lending logic supporting multiple books per student.
- Strict layered architecture (Controller / Logic / Persistence).
- PanacheRepository.Managed persistence pattern.
- MapStruct mapping layer.

## Tech Stack
- Quarkus 3.x, Kotlin 2.x
- PostgreSQL with Flyway migrations.