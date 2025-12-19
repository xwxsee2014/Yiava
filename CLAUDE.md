# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a personal research and testing repository for Java features, frameworks, tools, components, and middleware. The project is being built as a web application using:

- **JDK 17** (LTS)
- **Spring Boot 2.7** (latest 2.x version)
- **MyBatis** (SQL mapping framework)
- Standard Java web application stack

## Development Commands

This project uses **Maven** as the build tool (based on standard Spring Boot conventions).

### Build & Run
```bash
# Compile the project
mvn clean compile

# Run tests
mvn test

# Run a specific test class
mvn test -Dtest=ClassNameTest

# Run the application
mvn spring-boot:run

# Build JAR package
mvn clean package

# Build without running tests
mvn clean package -DskipTests

# Run the packaged JAR
java -jar target/*.jar
```

### Dependency Management
```bash
# Download dependencies
mvn dependency:resolve

# Update dependencies
mvn versions:display-dependency-updates

# Clean build (removes all compiled files and rebuilds)
mvn clean install
```

### Code Quality
```bash
# Run code style checks (if Checkstyle is configured)
mvn checkstyle:check

# Run static analysis (if SpotBugs is configured)
mvn spotbugs:check
```

## Project Structure

Once initialized, the project will follow standard Maven directory layout:

```
/d/projects/Yiava
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/yiava/          # Base package
│   │   │       ├── controller/     # REST controllers
│   │   │       ├── service/        # Business logic
│   │   │       ├── mapper/         # MyBatis mappers
│   │   │       ├── entity/         # Domain models
│   │   │       ├── dto/            # Data transfer objects
│   │   │       └── config/         # Configuration classes
│   │   └── resources/
│   │       ├── mapper/             # MyBatis XML mappers
│   │       ├── application.yml     # Spring Boot configuration
│   │       └── static/             # Static web resources
│   └── test/
│       └── java/                   # Test classes
├── pom.xml                         # Maven configuration
└── CLAUDE.md                       # This file
```

## Architecture Notes

### Layered Architecture
The application follows a typical layered architecture:

1. **Controller Layer** (REST API)
   - Handles HTTP requests/responses
   - Input validation
   - No business logic

2. **Service Layer** (Business Logic)
   - Contains business rules and workflows
   - Transaction management
   - Calls multiple mappers/repositories

3. **Mapper Layer** (Data Access)
   - MyBatis mappers for database access
   - SQL queries (XML or annotations)
   - Entity-to-DTO conversions

### Configuration

Key configuration files (when created):
- `src/main/resources/application.yml` - Main Spring Boot configuration
  - Database connection settings
  - MyBatis configuration
  - Server settings
  - Custom properties

### Database
- Uses MyBatis for SQL mapping
- Supports multiple database types (configurable via application.yml)
- Mapper XML files in `src/main/resources/mapper/`

### Testing Strategy
- Unit tests in `src/test/java/`
- Integration tests (when added) in separate package
- Use JUnit 5 and Spring Boot Test for testing
- Mockito for mocking dependencies

## Working with the Project

### Adding New Dependencies
Edit `pom.xml` and add dependencies in the `<dependencies>` section. Standard Spring Boot dependencies use:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-{name}</artifactId>
</dependency>
```

### Creating New Components
1. **New REST Endpoint**: Create in `src/main/java/com/yiava/controller/`
2. **New Service**: Create in `src/main/java/com/yiava/service/`
3. **New MyBatis Mapper**: Create interface in `src/main/java/com/yiava/mapper/` and XML in `src/main/resources/mapper/`

### Database Operations
- MyBatis mappers are Spring-managed beans
- Use `@MapperScan` in configuration to scan packages
- SQL statements can be in XML files or as annotations

## Important Configuration Properties

Common properties to configure in `application.yml` (when added):

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/yiava
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:password}
    driver-class-name: com.mysql.cj.jdbc.Driver

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.yiava.entity
  configuration:
    map-underscore-to-camel-case: true

server:
  port: 8080
  servlet:
    context-path: /api
```

## Tips for Development

1. **Hot Reload**: Use Spring Boot DevTools for automatic restart on code changes
2. **Database Migration**: Flyway is configured for schema versioning - migration scripts in `src/main/resources/db/migration/`
3. **API Documentation**: SpringDoc OpenAPI can be integrated for API docs
4. **Logging**: Logback is configured for structured logging - adjust levels in `logback.xml` or `application.yml`
5. **Health Checks**: Spring Actuator endpoints available at `/actuator/health`, `/actuator/info`, `/actuator/metrics`

## Research & Testing Focus

This repository is designed for experimenting with:
- Java 17 features (records, sealed classes, pattern matching, etc.)
- Spring Boot 2.7 capabilities
- Various Java frameworks and libraries
- Middleware integration (message queues, caching, etc.)
- Database technologies
- Authentication and security patterns

When adding new experimental features, create separate packages or modules to keep concerns organized.

---

**Last updated**: 2025-11-23

## Active Technologies

- Java 17 + Spring Boot 2.7 + MyBatis (main)
- Java 17 + Spring Boot 2.7 + Apache POI (002-docx-markdown-converter)

## Recent Changes

- 002-docx-markdown-converter: Added Java 17 + Spring Boot 2.7 + Apache POI
- 001-spring-boot-crud: Added Java 17 + Spring Boot 2.7 + MyBatis
