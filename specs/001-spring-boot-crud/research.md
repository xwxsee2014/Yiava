# Phase 0 Research: Spring Boot CRUD Implementation

**Date**: 2025-11-21
**Feature**: Spring Boot CRUD Init
**Branch**: 001-spring-boot-crud

## Research Questions & Findings

### 1. Spring Boot 2.7 Best Practices for API Development

**Question**: What are the current best practices for building REST APIs with Spring Boot 2.7?

**Decision**: Use `@RestController` with `@RequestMapping` for API endpoints, implement proper exception handling with `@ControllerAdvice`, use DTOs for request/response objects, and leverage Spring Boot's validation annotations (`@Valid`, `@NotBlank`, etc.).

**Rationale**: Spring Boot 2.7 is the latest 2.x version with stable APIs and extensive community support. Following these patterns ensures maintainable, testable, and standard-compliant REST APIs.

**Alternatives Considered**: Using `@Controller` + `ResponseEntity` (more verbose), using functional routing (less Spring-idiomatic).

---

### 2. MyBatis Integration with Spring Boot 2.7

**Question**: How to properly integrate MyBatis with Spring Boot 2.7 for data access?

**Decision**: Use `@MapperScan` annotation to auto-discover mapper interfaces, configure MyBatis in `application.yml` with mapper locations and type aliases, implement mappers with XML-based SQL queries for complex operations, use `@Insert`, `@Select`, `@Update`, `@Delete` annotations for simple queries.

**Rationale**: XML-based queries provide better separation of SQL from Java code and support complex queries. MyBatis is lightweight and gives direct control over SQL, which is ideal for this project.

**Alternatives Considered**: Spring Data JPA (more abstraction, less SQL control), JDBC Template (too low-level).

---

### 3. Druid Connection Pool Configuration

**Question**: How to configure Druid connection pool with Spring Boot 2.7 for production-ready database connections?

**Decision**: Add Druid starter dependency, configure in `application.yml` with connection pool settings (initial size, min/max pool size, connection timeout), enable Druid's built-in monitoring features (SQL slow query detection, connection leak detection), use HikariCP fallback configuration for Spring Boot's auto-configuration, enable Druid stat filters for monitoring.

**Rationale**: Druid provides superior monitoring, connection leak detection, and SQL analytics compared to default connection pools. It includes built-in dashboard for connection pool monitoring and is widely used in production environments.

**Alternatives Considered**: HikariCP (default, less monitoring), Tomcat CP (basic features), C3P0 (legacy).

---

### 4. Flyway Database Migration Strategy

**Question**: How to set up and manage database migrations with Flyway in a Spring Boot application?

**Decision**: Place migration scripts in `src/main/resources/db/migration/` with naming convention `V{version}__{description}.sql`, enable auto-migration on application startup via FlywayAutoConfiguration, use SQL-based migrations for simplicity, initialize with a script to create the content table.

**Rationale**: Flyway is the industry standard for database versioning in Java applications. SQL-based migrations are more straightforward than Java-based migrations and allow DBAs to review changes.

**Alternatives Considered**: Liquibase (more complex), manual SQL execution (error-prone, no versioning).

---

### 5. Logback Configuration for Production-Ready Logging

**Question**: What logging configuration ensures proper debuggability and monitoring?

**Decision**: Configure logback.xml with: console and file appenders, different log levels for different packages (DEBUG for app, INFO for Spring), rolling file policy with size and time-based rotation, structured logging pattern with timestamps and context, separate error logs.

**Rationale**: Logback is the default logging framework for Spring Boot. This configuration ensures logs are useful for both development and production debugging.

**Alternatives Considered**: Log4j2 (more configuration needed), external logging services (overkill for this project).

---

### 6. Spring Actuator Health Checks

**Question**: How to configure Spring Actuator for application monitoring?

**Decision**: Enable `management.endpoints.web.exposure.include=health,info,metrics`, create custom health indicators for database connectivity, configure health check URLs, add application metadata in `application.yml`.

**Rationale**: Spring Actuator provides production-ready monitoring features. Health checks are essential for load balancers and monitoring systems to detect application failures.

**Alternatives Considered**: Custom health check implementation (reinventing the wheel), external monitoring tools (too complex).

---

### 7. Modern Java 17 Patterns

**Question**: Which Java 17 features should be leveraged in this project?

**Decision**: Use `record` types for DTOs (immutable data carriers), use `Optional<T>` for nullable return values from services, use `var` for local variable type inference where type is obvious, use text blocks for multi-line SQL strings in XML files.

**Rationale**: These Java 17 features improve code readability and maintainability while following modern Java best practices as outlined in the Yiava Constitution.

**Alternatives Considered**: Traditional classes for DTOs (more boilerplate), null returns (less safe).

---

## Technology Integration Points

### Spring Boot + MyBatis + Flyway + Druid

- MyBatis auto-configuration works seamlessly with Flyway migrations
- Druid connection pool provides connection management for both MyBatis and Flyway
- Transaction management via `@Transactional` on service layer
- Druid monitoring integration with Spring Actuator for comprehensive observability
- Slow query detection helps identify performance bottlenecks in MyBatis queries

### Testing Strategy

- Unit tests: Service layer with mocked mappers
- Integration tests: Test with H2 in-memory database
- Contract tests: API tests with REST-assured or MockMvc

---

## Key Implementation Insights

1. **Layered Architecture Enforcement**: Controllers handle HTTP only, Services contain business logic, Mappers handle data access
2. **DTO vs Entity Separation**: Use DTOs for API contracts, entities for database mapping
3. **Error Handling**: Centralized exception handling with proper HTTP status codes
4. **Validation**: Bean Validation API for input validation at controller layer
5. **Configuration**: Externalize all configuration to `application.yml` for different environments

---

## Assumptions

- Using H2 database for development (in-memory or file-based)
- MySQL or PostgreSQL for production deployment
- Basic authentication or API key authentication (to be added later if needed)
- No internationalization (i18n) required for this feature
- Standard RESTful API conventions with JSON payloads
- No pagination required for list endpoint (can add later if needed)

---

## Next Steps

- Proceed to Phase 1 to design the data model and API contracts
- Begin implementation with foundational infrastructure (database, migrations, logging)
- Build CRUD operations following the layered architecture pattern
