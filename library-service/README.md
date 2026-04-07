# Library Service

A Spring Boot 4 + Kotlin microservice for managing library operations including students, books, and book loans.

## Overview

This service provides REST APIs for:
- **Students** - Register and manage student information
- **Books** - Catalog management for library books
- **Loans** - Track book borrowings and returns

## Technology Stack

- Spring Boot 4.0.3
- Kotlin 2.3.10
- JPA/Hibernate with PostgreSQL or H2
- MapStruct for DTO mapping
- Flyway for database migrations
- Kafka for event publishing
- Resilience4j for circuit breaking
- OpenTelemetry for distributed tracing

## Architecture

The service follows a layered architecture:

```
├── controller/           # REST API endpoints
│   ├── dto/             # Data Transfer Objects
├── extensions/          # Cross-cutting concerns
│   └── authorization    # Security & tenant management  
├── logic/               # Business logic layer
├── persistence/         # Data access layer
│   ├── entity/          # JPA Entities
│   ├── extensions/      # Audit trail, Kafka publisher
└── test/                # Unit, integration, architecture tests
```

## Local Development

### Prerequisites
- JDK 25
- Gradle 9.2.1+

### Start with H2 (in-memory database)
```bash
./gradlew bootRun
```

### Run Tests
```bash
./gradlew test
./gradlew integrationTest
```

### Generate Coverage Report
```bash
./gradlew jacocoTestReport
```

## API Endpoints

### Students
- `GET /students` - List students with pagination and search
- `GET /students/{id}` - Get student by ID
- `POST /students` - Create new student
- `PUT /students/{id}` - Update student
- `DELETE /students/{id}` - Delete student
- `GET /students/matriculation/{number}` - Find student by matriculation number

### Books
- `GET /books` - List books with pagination and search
- `GET /books/{id}` - Get book by ID
- `POST /books` - Add new book
- `PUT /books/{id}` - Update book
- `DELETE /books/{id}` - Delete book
- `GET /books/available` - List available books
- `GET /books/isbn/{isbn}` - Find book by ISBN

### Loans
- `POST /loans` - Create new loan (student borrows a book)
- `POST /loans/return?bookId={id}` - Return a borrowed book
- `GET /loans/student/{studentId}` - Get all loans for a student
- `GET /loans/book/{bookId}` - Get loan history for a book
- `GET /loans/active` - List active loans
- `GET /loans/overdue` - List overdue loans

## Multi-Tenancy

The service supports multi-tenancy:
- Each tenant has isolated database schema
- Tenant context extracted from HTTP headers (`X-TenantId`)
- All data is scoped to the requesting tenant

## Event Streaming

Domain events are published to Kafka topics:
- `student` - Student lifecycle events
- `book` - Book lifecycle events
- `loan` - Loan creation and return events

## Monitoring & Observability

- Health checks: `/actuator/health`
- Metrics (Prometheus): `/actuator/prometheus`
- OpenAPI documentation: `/v3/api-docs`, UI at `/swagger-ui.html`

## Build & Deploy

### JVM Image
```bash
./gradlew jibBuildImage
```

### Native Image (GraalVM)
```bash
./gradlew nativeCompile
./gradlew bootBuildImage --imageName=library-service-native
```

## Testing

- **Unit Tests**: Fast, isolated tests using mocks
- **Integration Tests**: Full stack with test database
- **Architecture Tests**: Enforce coding rules (ArchUnit)

Coverage reports generated in `build/reports/jacoco/test/index.html`
