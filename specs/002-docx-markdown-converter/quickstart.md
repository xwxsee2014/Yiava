# Quickstart Guide: Document to Markdown Conversion Service

**Date**: 2025-11-23
**Feature**: 002-docx-markdown-converter

## Overview

This guide will help you get up and running with the Document to Markdown Conversion Service quickly.

## Prerequisites

- **Java**: JDK 17 or higher
- **Maven**: 3.6 or higher
- **Database**: MySQL 8.0+ (or compatible 8.x versions)
- **Git**: For cloning the repository

## Setup Instructions

### 1. Clone and Checkout Feature Branch

```bash
git checkout 002-docx-markdown-converter
```

### 2. Configure Database

Create a database for the application:

```sql
CREATE DATABASE yiava_converter CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configure Application Properties

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/yiava_converter
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver

  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 50MB

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.yiava.entity
  configuration:
    map-underscore-to-camel-case: true

# Storage configuration
storage:
  root-path: ./storage
  document-retention-days: 90

# Logging configuration
logging:
  level:
    com.yiava: DEBUG
```

### 4. Install Dependencies

```bash
mvn clean install
```

### 5. Initialize Database Schema

```bash
mvn flyway:migrate
```

### 6. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Testing the API

### Upload and Convert a Document

```bash
curl -X POST http://localhost:8080/api/conversion/upload \
  -F "file=@/path/to/your/document.docx"
```

Response:
```json
{
  "documentId": 12345,
  "jobId": "job-67890",
  "status": "PROCESSING",
  "message": "Document uploaded successfully. Conversion in progress."
}
```

### Check Conversion Progress

```bash
curl http://localhost:8080/api/conversion/job-67890/status
```

Response:
```json
{
  "jobId": "job-67890",
  "documentId": 12345,
  "status": "IN_PROGRESS",
  "progressPercentage": 45,
  "currentPage": 5,
  "totalPages": 11,
  "startedAt": "2025-11-23T10:30:00Z"
}
```

### Download Converted Files

```bash
curl -o document-files.zip \
  http://localhost:8080/api/conversion/job-67890/download
```

### Query Conversion History

```bash
# List all documents
curl http://localhost:8080/api/documents

# Get specific document details
curl http://localhost:8080/api/documents/12345

# Download specific markdown file
curl -o page-1.md \
  http://localhost:8080/api/documents/12345/files/1
```

## API Documentation

OpenAPI specifications are available at:
- **Conversion API**: `http://localhost:8080/api/docs/conversion-api.yaml`
- **Query API**: `http://localhost:8080/api/docs/query-api.yaml`

You can also view interactive documentation at:
- **Swagger UI**: `http://localhost:8080/api/swagger-ui.html` (if SpringDoc is enabled)

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    Client (API Consumer)                    │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│                    Controller Layer                         │
│  DocumentController    QueryController                      │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│                     Service Layer                           │
│  DocumentConversionService  ProgressTrackingService         │
│  DocumentQueryService                                      │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│                      Mapper Layer                           │
│  DocumentMapper    ConversionJobMapper  MarkdownFileMapper  │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│                   Database (MyBatis)                        │
│  document  |  conversion_job  |  markdown_file              │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                 File System Storage                         │
│  storage/documents/{yyyy}/{mm}/{documentId}/                │
│  ├── original.docx                                         │
│  ├── page-1.md                                             │
│  ├── page-2.md                                             │
│  └── ...                                                   │
└─────────────────────────────────────────────────────────────┘
```

## Key Components

### Entities
- **Document**: Stores uploaded document metadata
- **ConversionJob**: Tracks conversion progress and status
- **MarkdownFile**: References to converted markdown files

### Services
- **DocumentConversionService**: Core conversion logic using Apache POI
- **ProgressTrackingService**: Manages async tasks and progress updates
- **DocumentQueryService**: Handles querying and retrieval of documents

### Controllers
- **DocumentController**: Upload, conversion, and download endpoints
- **QueryController**: Query and search endpoints

## Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=DocumentConversionServiceTest

# Run with coverage
mvn test jacoco:report
```

## Development Workflow

### 1. Make Changes

Edit files in:
- `src/main/java/com/yiava/` - Implementation code
- `src/test/` - Test code
- `src/main/resources/` - Configuration and mappers

### 2. Run Tests

```bash
mvn test
```

### 3. Build Package

```bash
mvn clean package
```

### 4. Run Integration Tests

```bash
mvn verify
```

## Troubleshooting

### Port Already in Use

```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

### Database Connection Issues

1. Verify database is running
2. Check credentials in `application.yml`
3. Ensure database exists
4. Check network connectivity

### Out of Memory Errors

Increase JVM heap size:
```bash
export JAVA_OPTS="-Xmx2g"
mvn spring-boot:run
```

### File Upload Fails

Check:
1. File size is under 50MB limit
2. File is DOC or DOCX format
3. File is not password-protected
4. Storage directory is writable

## Common Issues

### Page Extraction Not Working

**Symptom**: Conversion completes but all content is in one file

**Solution**: Verify page break detection logic in `DocumentConversionService`

### Progress Not Updating

**Symptom**: Progress stays at 0% throughout conversion

**Solution**: Check `@Async` configuration and `CompletableFuture` updates

### Files Not Downloadable

**Symptom**: Download endpoint returns 404

**Solution**: Verify filesystem storage path and file permissions

## Next Steps

1. Review the full [Feature Specification](./spec.md)
2. Review the [Implementation Plan](./plan.md)
3. Review the [Data Model](./data-model.md)
4. Review the [Research Report](./research.md)
5. Review the [API Contracts](./contracts/)
6. Run tests to ensure everything works
7. Review code coverage and quality metrics

## Support

For issues and questions:
- Check existing tests for examples
- Review API documentation
- Consult the specification documents
- Check application logs at `DEBUG` level

## Performance Tips

- Use connection pooling (enabled by default in Spring Boot)
- Enable query result caching for frequently accessed documents
- Monitor database performance with slow query logging
- Consider partitioning storage by year/month for large datasets
- Use pagination for query endpoints (default 20, max 100)
