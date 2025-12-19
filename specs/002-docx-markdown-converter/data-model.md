# Data Model: Document to Markdown Conversion Service

**Date**: 2025-11-23
**Feature**: 002-docx-markdown-converter

## Entity Definitions

### 1. Document (Entity)
Represents an uploaded document and its metadata.

```java
public record Document(
    Long id,
    String originalFilename,
    String fileExtension,
    Long fileSize,
    LocalDateTime uploadTime,
    Integer pageCount,
    String storagePath,
    DocumentStatus status,
    String errorMessage
) {
    public enum DocumentStatus {
        UPLOADED,      // Just uploaded, not yet processing
        PROCESSING,    // Currently being converted
        COMPLETED,     // Successfully converted
        FAILED         // Conversion failed
    }
}
```

**Validation Rules**:
- `originalFilename`: Not null, max 255 characters
- `fileExtension`: Must be "doc" or "docx"
- `fileSize`: Must be > 0 and < 50MB (configurable)
- `pageCount`: Must be > 0

**Relationships**:
- One-to-many with `ConversionJob` (single document may have multiple conversion attempts)
- One-to-many with `MarkdownFile` (multiple pages per document)

---

### 2. ConversionJob (Entity)
Tracks the progress and status of a document conversion task.

```java
public record ConversionJob(
    Long id,
    Long documentId,
    JobStatus status,
    Integer progressPercentage,
    Integer currentPage,
    Integer totalPages,
    LocalDateTime startedAt,
    LocalDateTime completedAt,
    String errorMessage
) {
    public enum JobStatus {
        PENDING,       // Waiting to start
        IN_PROGRESS,   // Currently processing
        COMPLETED,     // Finished successfully
        FAILED,        // Failed with error
        CANCELLED      // Manually cancelled
    }
}
```

**Validation Rules**:
- `progressPercentage`: 0-100
- `currentPage`: 0 to `totalPages`
- `startedAt`: Must be before `completedAt` (if both present)

**Relationships**:
- Many-to-one with `Document`
- Cascade on delete (removing document removes all its jobs)

---

### 3. MarkdownFile (Entity)
Represents a single converted markdown file (one per page).

```java
public record MarkdownFile(
    Long id,
    Long documentId,
    Integer pageNumber,
    String filePath,
    Long fileSize,
    String filename,
    LocalDateTime createdAt
) {
    // Derived: pageNumber determines sort order
    // Derived: filename = "{originalName}-page-{pageNumber}.md"
}
```

**Validation Rules**:
- `pageNumber`: Must be > 0, unique per document
- `filePath`: Not null, must exist on filesystem
- `filename`: Not null, follows naming convention

**Relationships**:
- Many-to-one with `Document`
- Unique constraint on (documentId, pageNumber)

---

### 4. DocumentMetadata (DTO)
Aggregated metadata for API responses (User Story 5).

```java
public record DocumentMetadata(
    Long id,
    String originalFilename,
    String fileExtension,
    Long fileSize,
    Integer pageCount,
    LocalDateTime uploadTime,
    DocumentStatus status,
    List<MarkdownFile> markdownFiles,
    Optional<ConversionJob> latestJob
)
```

**Usage**:
- Returned by query endpoints (FR-013, FR-014)
- Includes calculated fields (e.g., total converted size)
- May include pre-loaded relationships for performance

---

## Database Schema

**Database**: MySQL 8.x

### Tables

#### document
```sql
CREATE TABLE document (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    original_filename VARCHAR(255) NOT NULL,
    file_extension VARCHAR(10) NOT NULL CHECK (file_extension IN ('doc', 'docx')),
    file_size BIGINT NOT NULL,
    upload_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    page_count INT NOT NULL,
    storage_path VARCHAR(500) NOT NULL,
    status ENUM('UPLOADED', 'PROCESSING', 'COMPLETED', 'FAILED') NOT NULL,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_upload_time (upload_time),
    INDEX idx_status (status)
);
```

#### conversion_job
```sql
CREATE TABLE conversion_job (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    document_id BIGINT NOT NULL,
    status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED', 'FAILED', 'CANCELLED') NOT NULL,
    progress_percentage INT DEFAULT 0 CHECK (progress_percentage >= 0 AND progress_percentage <= 100),
    current_page INT DEFAULT 0,
    total_pages INT NOT NULL,
    started_at TIMESTAMP NULL,
    completed_at TIMESTAMP NULL,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (document_id) REFERENCES document(id) ON DELETE CASCADE,
    INDEX idx_document_id (document_id),
    INDEX idx_status (status)
);
```

#### markdown_file
```sql
CREATE TABLE markdown_file (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    document_id BIGINT NOT NULL,
    page_number INT NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    filename VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (document_id) REFERENCES document(id) ON DELETE CASCADE,
    UNIQUE KEY uk_document_page (document_id, page_number),
    INDEX idx_document_id (document_id)
);
```

---

## State Transitions

### Document Status Flow
```
UPLOADED → PROCESSING → COMPLETED
                ↓
              FAILED
```

**Valid Transitions**:
- UPLOADED → PROCESSING: When conversion starts
- PROCESSING → COMPLETED: On successful conversion
- PROCESSING → FAILED: On conversion error
- Any → FAILED: On unrecoverable error

### ConversionJob Status Flow
```
PENDING → IN_PROGRESS → COMPLETED
              ↓
            FAILED
              ↓
          CANCELLED
```

**Valid Transitions**:
- PENDING → IN_PROGRESS: When task executor starts job
- IN_PROGRESS → COMPLETED: On successful conversion
- IN_PROGRESS → FAILED: On conversion error
- IN_PROGRESS → CANCELLED: On manual cancellation
- PENDING → CANCELLED: If cancelled before starting

---

## Data Validation Rules

### Upload Validation
1. File extension must be "doc" or "docx"
2. File size must be > 0 and < 50MB (configurable)
3. File must not be password-protected (detected during parsing)
4. Document must contain at least 1 page

### Conversion Validation
1. Document must be in PROCESSING state to create job
2. Only one IN_PROGRESS job allowed per document
3. Page count must match actual document pages
4. All markdown files must be created before marking complete

### Storage Validation
1. All storage paths must exist and be writable
2. Markdown files must follow naming convention
3. No duplicate page numbers per document
4. Storage cleanup after 90 days (SC-009)

---

## Query Patterns

### By Document ID
```sql
SELECT d.*, mf.*, cj.*
FROM document d
LEFT JOIN markdown_file mf ON d.id = mf.document_id
LEFT JOIN conversion_job cj ON d.id = cj.document_id
WHERE d.id = ?
```

### By Upload Date Range
```sql
SELECT id, original_filename, upload_time, status, page_count
FROM document
WHERE upload_time BETWEEN ? AND ?
ORDER BY upload_time DESC
```

### By Filename Search
```sql
SELECT id, original_filename, upload_time, status, page_count
FROM document
WHERE original_filename LIKE CONCAT('%', ?, '%')
ORDER BY upload_time DESC
```

### Latest Conversion Status
```sql
SELECT cj.*
FROM conversion_job cj
WHERE cj.document_id = ?
ORDER BY cj.created_at DESC
LIMIT 1
```

---

## Indexes and Performance

**Critical Indexes**:
1. `document.upload_time` - For date range queries
2. `document.status` - For filtering by status
3. `conversion_job.document_id` - For joining to document
4. `markdown_file.document_id, page_number` - For page retrieval

**Query Performance Targets**:
- Document metadata query: < 2 seconds (SC-010)
- Conversion status lookup: < 500ms
- Full document with files: < 5 seconds

**Optimization Strategies**:
- Use pagination for large result sets
- Consider caching frequently accessed documents
- Lazy load markdown file lists
- Index on upload_time for archival queries

---

## Data Retention Policy

**Active Documents** (0-90 days):
- Full metadata retained
- Original files and markdown files accessible
- Query performance prioritized

**Archived Documents** (90+ days):
- Original files deleted from filesystem
- Metadata retained for 1 year
- Markdown files may be moved to cold storage
- Query returns metadata only

**Cleanup Strategy**:
```sql
-- Daily cleanup job
DELETE FROM markdown_file
WHERE document_id IN (
    SELECT id FROM document
    WHERE upload_time < DATE_SUB(NOW(), INTERVAL 90 DAY)
);

DELETE FROM document
WHERE upload_time < DATE_SUB(NOW(), INTERVAL 365 DAY);
```

---

## Non-Functional Requirements

**Scalability**:
- Support 1000+ concurrent documents
- Query pagination (default 20, max 100)
- Connection pooling for database

**Reliability**:
- Transactions for all multi-table operations
- Retry logic for transient failures
- Graceful handling of partial conversions

**Security**:
- Input validation on all parameters
- SQL injection prevention via MyBatis
- File path traversal prevention
- File size and type restrictions
