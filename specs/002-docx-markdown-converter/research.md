# Research Report: Document to Markdown Conversion

**Date**: 2025-11-23
**Feature**: Document to Markdown Conversion Service (002-docx-markdown-converter)

## Research Questions & Findings

### Q1: How to extract page boundaries from DOC/DOCX files using Apache POI?

**Decision**: Use Apache POI's `XWPFDocument` for DOCX and `HWPFDocument` for DOC files, leveraging page break markers.

**Rationale**: Apache POI provides native support for Word document parsing. Page breaks can be detected using paragraph formatting properties and section breaks.

**Implementation Approach**:
- For DOCX: Use `XWPFDocument` and iterate through paragraphs, detecting `isPageBreak()` or section breaks
- For DOC: Use `HWPFDocument` with similar page break detection
- Track paragraph positions to split content at page boundaries

**Alternatives Considered**:
- Converting to PDF then parsing: Rejected - adds complexity and potential formatting loss
- Using third-party libraries (Aspose.Words): Rejected - proprietary and expensive
- Page count estimation via rendered pages: Rejected - unreliable without actual rendering

**Reference**: Apache POI 5.4.1 documentation on document model and page break handling

---

### Q2: Best practices for converting Word document elements to Markdown?

**Decision**: Implement element-by-element conversion mapping: paragraphs, headings, tables, lists, images, and text formatting.

**Rationale**: Markdown has well-defined syntax for common document elements. Direct mapping preserves structure while ensuring compatibility.

**Conversion Mapping**:
- **Headings**: `XWPFHeading` → `#`, `##`, `###` based on heading level
- **Paragraphs**: Direct text with preserved line breaks
- **Bold/Italic**: `XWPFRun.isBold()`, `XWPFRun.isItalic()` → `**bold**`, `*italic*`
- **Tables**: `XWPFTable` → pipe-separated markdown table format
- **Lists**: `XWPFParagraph.getNumLevel()` → `-` or `1.` for markdown lists
- **Images**: Extract and save separately, reference with `![alt](path)`
- **Page Breaks**: Split content into separate files

**Special Handling**:
- **Headers/Footers**: Extract and discard during page content parsing (FR-010)
- **Special Characters**: Ensure UTF-8 encoding preservation (FR-008)
- **Nested Lists**: Track indentation levels for proper markdown nesting

**Alternatives Considered**:
- HTML intermediate format: Rejected - adds unnecessary transformation step
- Markdown extensions (tables, footnotes): Rejected - reduces portability
- Plain text conversion: Rejected - loses document structure (FR-004)

---

### Q3: How to implement real-time progress tracking for long document conversions?

**Decision**: Use Spring Boot `@Async` with `CompletableFuture` and Server-Sent Events (SSE) or periodic polling endpoint.

**Rationale**: Async processing prevents blocking requests while SSE provides real-time updates without WebSocket complexity.

**Implementation Pattern**:
1. Start conversion as async task returning `CompletableFuture<String>` (job ID)
2. Store progress in database with conversion status (IN_PROGRESS, percentage, current page)
3. Provide two progress retrieval options:
   - **SSE Endpoint**: `GET /api/conversion/{id}/progress` (real-time streaming)
   - **Polling Endpoint**: `GET /api/conversion/{id}/status` (returns JSON with progress)

**Progress Tracking Details**:
- Update progress per page: `(currentPage / totalPages) * 100`
- Store: job ID, status (PENDING/IN_PROGRESS/COMPLETED/FAILED), current page, total pages, error message
- Progress updates for documents with 10+ pages (SC-008)

**Alternatives Considered**:
- WebSocket: Rejected - adds complexity, not needed for one-way updates
- Long polling: Rejected - less efficient than SSE
- Return progress in upload response: Rejected - doesn't work for large documents

---

### Q4: What is the optimal storage strategy for documents and converted files?

**Decision**: Hybr id approach - database for metadata (MyBatis) + filesystem for markdown files.

**Rationale**: Database provides efficient querying and indexing for metadata, while filesystem offers simple file storage and retrieval.

**Storage Architecture**:
- **Database Tables**:
  - `document`: id, filename, original_name, file_size, upload_time, page_count, storage_path, status
  - `conversion_job`: id, document_id, status, progress_percentage, current_page, started_at, completed_at, error_message
  - `markdown_file`: id, document_id, page_number, file_path, file_size

- **Filesystem Structure**:
  ```
  storage/
  └── documents/
      └── {yyyy}/
          └── {mm}/
              └── {documentId}/
                  ├── original.docx
                  ├── page-1.md
                  ├── page-2.md
                  └── ...
  ```

**Metadata Storage** (MyBatis):
- Upload timestamp, filename, file size, page count
- Conversion status and progress
- Storage path for file retrieval
- 90-day retention policy tracking

**Alternatives Considered**:
- Database blob storage: Rejected - bloats database, poor performance
- Cloud storage (S3): Rejected - adds external dependency for MVP
- In-memory storage: Rejected - no persistence (violates SC-009)

---

### Q5: How to handle file naming and organization for converted markdown files?

**Decision**: Use descriptive naming pattern: `{documentName-page-N.md}` with hierarchical directory structure.

**Rationale**: Descriptive names aid identification and debugging. Hierarchical structure prevents filesystem issues with too many files in one directory.

**Naming Convention**:
- **Original Document**: `{documentId}_original.{ext}`
- **Markdown Pages**: `{originalName}-page-{pageNumber}.md`
  - Example: `report-2023-page-1.md`, `report-2023-page-2.md`

**Directory Organization**:
- Group by year/month to distribute files
- Each document gets unique subdirectory
- Prevents filename conflicts and improves organization

**Database Reference**:
- Store both logical filename and physical path
- Enables easy retrieval and download
- Supports filename-based search queries (User Story 5, scenario 3)

**Alternatives Considered**:
- UUID-based names: Rejected - reduces usability
- Sequential numbering only: Rejected - loses document context
- Flat directory structure: Rejected - scalability issues

---

## Key Technologies & Dependencies

### Core Libraries
- **Apache POI 5.4.1**: Document parsing and text extraction
- **Spring Boot 2.7**: Web framework and async processing
- **MyBatis 3.x**: Database ORM for metadata persistence
- **MySQL 8.x**: Relational database for structured data

### Spring Boot Starters
- `spring-boot-starter-web`: REST API and HTTP handling
- `spring-boot-starter-data-mybatis`: MyBatis integration
- `spring-boot-starter-actuator`: Monitoring and health checks

### Java 17 Features to Leverage
- **Records**: DTOs for API responses and data transfer
- **Optional**: Null-safe handling in service layer
- **Streams**: Processing document elements and file lists
- **CompletableFuture**: Async conversion and progress tracking

---

## Implementation Recommendations

1. **Start with DOCX support** (XWPFDocument) before adding DOC compatibility
2. **Use Spring Boot's async task execution** for non-blocking document processing
3. **Implement pagination** for query results (User Story 5 scenario 3)
4. **Add validation** for file uploads (size limits, format checking)
5. **Use transactions** in service layer for data consistency
6. **Implement proper error handling** with meaningful error messages (FR-006, FR-007)

---

## Unknowns Resolved

All research questions have been answered with specific implementation approaches. No further clarification needed before Phase 1 design.
