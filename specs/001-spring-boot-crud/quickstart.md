# Quick Start Guide: Spring Boot CRUD Application

**Date**: 2025-11-21
**Version**: 1.0.0

## Overview

This guide will help you get the Spring Boot CRUD application up and running quickly. The application provides REST API endpoints for managing content records with a MyBatis data access layer and Flyway database migrations.

## Prerequisites

- **Java**: JDK 17 or later
- **Maven**: 3.6 or later
- **Database**: H2 (development), MySQL/PostgreSQL (production)

## Building the Application

```bash
# Clone the repository
git clone <repository-url>
cd yiava

# Switch to the feature branch
git checkout 001-spring-boot-crud

# Build the application
mvn clean package
```

## Running the Application

### Option 1: Maven Spring Boot Plugin

```bash
# Run the application
mvn spring-boot:run
```

### Option 2: JAR File

```bash
# Run the packaged JAR
java -jar target/yiava-*.jar
```

### Option 3: IDE

Run the main class: `com.yiava.YiavaApplication`

## Configuration

### Database Configuration

The application uses H2 in-memory database by default with Druid connection pool. For production, configure your database in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb  # For H2
    # url: jdbc:mysql://localhost:3306/yiava  # For MySQL
    # url: jdbc:postgresql://localhost:5432/yiava  # For PostgreSQL
    username: sa
    password:
    driver-class-name: org.h2.Driver  # MySQL: com.mysql.cj.jdbc.Driver

    # Druid Connection Pool Configuration
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
      initial-size: 5
      min-idle: 5
      max-active: 20
      max-wait: 60000
      time-between-eviction-runs-millis: 60000
      min-evictable-idle-time-millis: 300000
      validation-query: SELECT 1 FROM DUAL
      test-while-idle: true
      test-on-borrow: false
      test-on-return: false
      pool-prepared-statements: true
      max-pool-prepared-statement-per-connection-size: 20
      filters: stat,wall,log4j
      connection-properties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
```

**Druid Monitoring**: Access the Druid monitoring console at `http://localhost:8080/druid/index.html` (for debugging purposes - disable in production)

### Actuator Configuration

Access monitoring endpoints at:

- **Health Check**: `http://localhost:8080/actuator/health`
- **Application Info**: `http://localhost:8080/actuator/info`
- **Metrics**: `http://localhost:8080/actuator/metrics`

## API Usage Examples

### Create Content (POST /api/content)

**Request**:
```bash
curl -X POST http://localhost:8080/api/content \
  -H "Content-Type: application/json" \
  -d '{"content": "Hello, World!"}'
```

**Response** (201 Created):
```json
{
  "id": 1,
  "content": "Hello, World!",
  "createdAt": "2025-11-21T10:30:00",
  "updatedAt": "2025-11-21T10:30:00"
}
```

### Get All Content (GET /api/content)

**Request**:
```bash
curl http://localhost:8080/api/content
```

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "content": "Hello, World!",
    "createdAt": "2025-11-21T10:30:00",
    "updatedAt": "2025-11-21T10:30:00"
  }
]
```

### Get Content by ID (GET /api/content/{id})

**Request**:
```bash
curl http://localhost:8080/api/content/1
```

**Response** (200 OK):
```json
{
  "id": 1,
  "content": "Hello, World!",
  "createdAt": "2025-11-21T10:30:00",
  "updatedAt": "2025-11-21T10:30:00"
}
```

### Update Content (PUT /api/content/{id})

**Request**:
```bash
curl -X PUT http://localhost:8080/api/content/1 \
  -H "Content-Type: application/json" \
  -d '{"content": "Updated content"}'
```

**Response** (200 OK):
```json
{
  "id": 1,
  "content": "Updated content",
  "createdAt": "2025-11-21T10:30:00",
  "updatedAt": "2025-11-21T10:31:00"
}
```

### Delete Content (DELETE /api/content/{id})

**Request**:
```bash
curl -X DELETE http://localhost:8080/api/content/1
```

**Response** (204 No Content): No content returned

## Testing

### Run All Tests

```bash
# Run unit and integration tests
mvn test

# Run specific test class
mvn test -Dtest=ContentServiceTest
```

### Test Coverage

```bash
# Generate test coverage report
mvn jacoco:report
# View report at: target/site/jacoco/index.html
```

## Logging

### Log Files

- **Console**: Logs appear in the console when running the application
- **File**: Logs are written to `logs/yiava.log` (when configured)
- **Logback Config**: `src/main/resources/logback.xml`

### Log Levels

Default log levels:
- Application packages: DEBUG
- Spring framework: INFO
- SQL queries: DEBUG (via MyBatis configuration)

### Viewing Logs

```bash
# Follow logs in real-time
tail -f logs/yiava.log
```

## Database Migrations

Flyway automatically runs migration scripts on startup. Migration files are located in:

`src/main/resources/db/migration/`

Naming convention: `V{version}__{description}.sql`

Example: `V1__Create_content_table.sql`

### Manual Migration

```bash
# Repair migration history (if needed)
mvn flyway:repair

# Run migrations manually
mvn flyway:migrate
```

## Troubleshooting

### Port Already in Use

If port 8080 is already in use, change the port in `application.yml`:

```yaml
server:
  port: 8081
```

### Database Connection Issues

1. Verify database credentials in `application.yml`
2. Ensure database is running and accessible
3. Check Flyway migration status:

```bash
mvn flyway:info
```

### Health Check Failing

Access `/actuator/health` to see detailed health information:

```bash
curl http://localhost:8080/actuator/health
```

### Druid Connection Pool Issues

1. **Monitor Connection Pool**: Access Druid console at `/druid/index.html` to view active connections, pool statistics, and SQL monitoring
2. **Check for Connection Leaks**: Druid automatically detects and logs connection leaks - check application logs
3. **Slow Query Detection**: Queries exceeding 5 seconds are logged automatically
4. **Adjust Pool Size**: Modify `initial-size`, `min-idle`, and `max-active` based on your application's concurrent users

### Enable Debug Logging

Add to `application.yml`:

```yaml
logging:
  level:
    com.yiava: DEBUG
    org.springframework: DEBUG
    org.mybatis: DEBUG
```

## Development Workflow

### Hot Reload

Spring Boot DevTools automatically reloads the application when files change.

### Maven Commands Reference

```bash
# Clean build
mvn clean

# Compile
mvn compile

# Run tests
mvn test

# Package JAR
mvn package

# Run with Spring Boot
mvn spring-boot:run

# Skip tests during build
mvn package -DskipTests
```

## Next Steps

1. **Extend the API**: Add pagination, filtering, sorting to GET /api/content
2. **Add Authentication**: Implement API key or JWT-based authentication
3. **Add Categories**: Introduce content categorization
4. **Add Validation**: Implement custom validation rules
5. **Add Monitoring**: Integrate with Prometheus/Grafana
6. **Add Docker**: Create containerized deployment
7. **Add CI/CD**: Set up automated build and deployment pipeline

## Support

For issues and questions:
- Review application logs for error messages
- Check `/actuator/health` endpoint for system status
- Review Flyway migration status
- Consult the project documentation in `docs/`

---

**Note**: This quickstart covers the basic setup. For production deployment, additional configurations for security, monitoring, and scalability should be implemented.
