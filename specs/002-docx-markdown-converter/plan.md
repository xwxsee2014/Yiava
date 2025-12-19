# Implementation Plan: Document to Markdown Conversion Service

**Branch**: `002-docx-markdown-converter` | **Date**: 2025-11-23 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/002-docx-markdown-converter/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/commands/plan.md` for the execution workflow.

## Summary

Build a Spring Boot 2.7 web application that converts DOC/DOCX documents to markdown format using Apache POI. Each page of the source document becomes a separate markdown file. The system provides REST APIs for document upload, conversion, progress tracking, and querying conversion history. All converted files are persistently stored for 90 days with query capabilities for metadata and download.

**Technical Approach**: Use Apache POI for document parsing, split content by page boundaries, remove headers/footers, and generate markdown files with descriptive naming. Store conversion records in database via MyBatis and markdown files on filesystem. Provide real-time progress updates for large documents.

## Technical Context

**Language/Version**: Java 17 (JDK 17 LTS)
**Primary Dependencies**: Spring Boot 2.7, Apache POI (document processing), MyBatis (SQL mapping), Maven (build)
**Storage**: MySQL 8.x database (via MyBatis) for metadata + filesystem for markdown files
**Testing**: JUnit 5, Spring Boot Test, Mockito (unit/integration/contract tests)
**Target Platform**: Spring Boot web application (REST API)
**Project Type**: Single web application
**Performance Goals**: Convert 10-page document in under 30 seconds, query metadata within 2 seconds
**Constraints**: 90-day file retention, single document upload per request, progress tracking for 10+ pages
**Scale/Scope**: Single-user document conversion service, supports up to 100+ pages per document

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

**Gates derived from Yiava Constitution principles:**

- [x] **Research-First**: Research Apache POI capabilities for page extraction and markdown conversion patterns
- [x] **Java Best Practices**: Modern Java 17 patterns (records for DTOs, Optional for null-safety, streams for processing)
- [x] **Layered Architecture**: Component separation defined (Controller/Service/Mapper layers with DTOs)
- [x] **Testing Strategy**: Test plan established - unit tests for services, integration tests for MyBatis, contract tests for APIs
- [x] **Documentation Plan**: Documentation approach defined (CLAUDE.md updates, SpringDoc/OpenAPI for REST docs)
- [x] **Simplicity**: No speculative design - follows standard Spring Boot conventions

## Project Structure

### Documentation (this feature)

```text
specs/002-docx-markdown-converter/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
│   ├── conversion-api.yaml
│   └── query-api.yaml
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)

```text
src/
├── main/
│   ├── java/
│   │   └── com/yiava/
│   │       ├── controller/     # REST controllers (HTTP concerns only)
│   │       │   ├── DocumentController.java
│   │       │   └── QueryController.java
│   │       ├── service/        # Business logic and workflows
│   │       │   ├── DocumentConversionService.java
│   │       │   ├── ProgressTrackingService.java
│   │       │   └── DocumentQueryService.java
│   │       ├── mapper/         # MyBatis data access
│   │       │   ├── DocumentMapper.java
│   │       │   └── ConversionJobMapper.java
│   │       ├── entity/         # Domain models
│   │       │   ├── Document.java
│   │       │   ├── ConversionJob.java
│   │       │   └── DocumentMetadata.java
│   │       ├── dto/            # Data transfer objects
│   │       │   ├── UploadResponse.java
│   │       │   ├── ConversionResult.java
│   │       │   └── DocumentInfo.java
│   │       ├── config/         # Configuration classes
│   │       │   ├── MyBatisConfig.java
│   │       │   └── StorageConfig.java
│   │       └── YiavaApplication.java
│   └── resources/
│       ├── mapper/             # MyBatis XML mappers
│       │   ├── DocumentMapper.xml
│       │   └── ConversionJobMapper.xml
│       ├── application.yml     # Spring Boot configuration
│       └── static/             # Static resources
└── test/
    ├── unit/                   # Unit tests (JUnit 5 + Mockito)
    ├── integration/            # Integration tests (Spring Boot Test)
    └── contract/               # Contract tests (API validation)
```

**Structure Decision**: Single Spring Boot 2.7 application following standard Maven layout with layered architecture (Controller/Service/Mapper). This structure aligns with Yiava Constitution Principle III (Layered Architecture) and Spring Boot best practices.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| N/A | N/A | N/A |

---

**Phase 0**: Research Apache POI page extraction, markdown conversion best practices, and Spring Boot async processing for progress tracking
**Phase 1**: Design data model, API contracts, and implementation approach with MyBatis integration
