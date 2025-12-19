---

description: "Task list for Document to Markdown Conversion Service implementation"
---

# Tasks: Document to Markdown Conversion Service

**Input**: Design documents from `/specs/002-docx-markdown-converter/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/

**Organization**: Tasks are grouped by user story to enable independent implementation and testing.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic Spring Boot structure

- [x] T001 Create Spring Boot project structure at repository root (src/main/java/com/yiava/, src/main/resources/, src/test/)
- [x] T002 Add Maven dependencies (Spring Boot 2.7, Apache POI 5.4.1, MyBatis, MySQL driver, JUnit 5, Spring Boot Test, Mockito) in pom.xml
- [x] T003 Create application.yml with database and storage configuration at src/main/resources/application.yml
- [x] T004 Create main application class YiavaApplication.java at src/main/java/com/yiava/YiavaApplication.java
- [x] T005 [P] Setup Flyway migration framework with migration files in classpath:db/migration/
- [x] T006 [P] Create application logging configuration at src/main/resources/logback.xml
- [x] T007 [P] Create .gitignore entries for storage/ and target/ directories

**Checkpoint**: Spring Boot project ready with dependencies ✅

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core database and infrastructure - MUST complete before ANY user story

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [x] T008 Create database schema migration V2__Create_docx_converter_schema.sql at src/main/resources/db/migration/
  - document table (id, original_filename, file_extension, file_size, upload_time, page_count, storage_path, status, error_message)
  - conversion_job table (id, document_id, status, progress_percentage, current_page, total_pages, started_at, completed_at, error_message)
  - markdown_file table (id, document_id, page_number, file_path, file_size, filename, created_at)
- [x] T009 Create MyBatis configuration class MyBatisConfig.java at src/main/java/com/yiava/config/MyBatisConfig.java
- [x] T010 Create entity classes in src/main/java/com/yiava/entity/:
  - T010a Document.java (Document record with status enum)
  - T010b ConversionJob.java (ConversionJob record with status enum)
  - T010c MarkdownFile.java (MarkdownFile record)
- [x] T011 Create MyBatis mapper interfaces in src/main/java/com/yiava/mapper/:
  - T011a DocumentMapper.java
  - T011b ConversionJobMapper.java
  - T011c MarkdownFileMapper.java
- [x] T012 Create MyBatis XML mapper files in src/main/resources/mapper/:
  - T012a DocumentMapper.xml
  - T012b ConversionJobMapper.xml
  - T012c MarkdownFileMapper.xml
- [x] T013 Create StorageConfig.java at src/main/java/com/yiava/config/StorageConfig.java for filesystem management
- [x] T014 Create global exception handler GlobalExceptionHandler.java at src/main/java/com/yiava/exception/GlobalExceptionHandler.java
- [x] T015 Create storage directory initialization logic in StorageService.java at src/main/java/com/yiava/service/StorageService.java

**Checkpoint**: Database, entities, mappers, and storage infrastructure ready - user story implementation can begin ✅

---

## Phase 3: User Story 1 - Upload Document and Receive Markdown Files (Priority: P1) 🎯 MVP

**Goal**: Upload DOC/DOCX documents, convert each page to markdown, and provide download

**Independent Test**: Upload a multi-page DOCX file and verify markdown files are generated and downloadable

### DTOs for User Story 1

- [ ] T016 [P] [US1] Create UploadResponse DTO at src/main/java/com/yiava/dto/UploadResponse.java
- [ ] T017 [P] [US1] Create ConversionResult DTO at src/main/java/com/yiava/dto/ConversionResult.java

### Services for User Story 1

- [ ] T018 [US1] Implement DocumentConversionService.java at src/main/java/com/yiava/service/DocumentConversionService.java
  - Load document with Apache POI
  - Extract page boundaries
  - Remove headers/footers
  - Convert content to markdown
  - Save markdown files to storage
  - Update Document and MarkdownFile records
- [ ] T019 [US1] Implement DocumentStorageService.java at src/main/java/com/yiava/service/DocumentStorageService.java
  - Save original file to storage directory
  - Create markdown files in organized structure
  - Manage storage paths and cleanup

### Controllers for User Story 1

- [ ] T020 [US1] Implement DocumentController.java at src/main/java/com/yiava/controller/DocumentController.java
  - POST /api/conversion/upload - Accept multipart file upload
  - GET /api/conversion/{jobId}/download - Download converted markdown files as ZIP
  - GET /api/conversion/{jobId}/status - Get conversion status

### Integration for User Story 1

- [ ] T021 [US1] Integrate DocumentController with DocumentConversionService
- [ ] T022 [US1] Test complete upload-conversion-download flow with sample DOCX file
- [ ] T023 [US1] Verify page-by-page markdown generation works correctly

**Checkpoint**: User Story 1 complete - Can upload, convert, and download markdown files

---

## Phase 4: User Story 2 - Handle Various Document Types and Structures (Priority: P1)

**Goal**: Preserve document structure (headings, tables, lists, formatting) and handle different file types

**Independent Test**: Upload documents with various elements (headings, tables, images, lists) and verify markdown preserves structure

### Enhanced Services for User Story 2

- [ ] T024 [US2] Enhance DocumentConversionService to preserve:
  - Headings (H1-H6) using # syntax
  - Bold/italic text using **bold** and *italic* syntax
  - Bulleted and numbered lists
  - Tables in markdown pipe format
  - Images with placeholder or reference links
  - Special characters and UTF-8 encoding
- [ ] T025 [P] [US2] Create MarkdownConverter utility at src/main/java/com/yiava/util/MarkdownConverter.java
  - Convert POI document elements to markdown
  - Handle nested structures
  - Preserve formatting fidelity

### Unit Tests for User Story 2

- [ ] T026 [P] [US2] Unit test DocumentConversionService at src/test/java/yiava/service/DocumentConversionServiceTest.java
- [ ] T027 [P] [US2] Unit test MarkdownConverter at src/test/java/yiava/util/MarkdownConverterTest.java

### Integration for User Story 2

- [ ] T028 [US2] Test document with headings and verify markdown structure
- [ ] T029 [US2] Test document with tables and verify markdown table conversion
- [ ] T030 [US2] Test document with special characters (Chinese, symbols)
- [ ] T031 [US2] Test both DOC and DOCX formats work correctly

**Checkpoint**: User Story 2 complete - Document structure and formatting preserved in markdown

---

## Phase 5: User Story 3 - Error Handling and User Feedback (Priority: P2)

**Goal**: Provide clear error messages for invalid files and handle edge cases gracefully

**Independent Test**: Upload invalid inputs (wrong format, corrupted file, password-protected) and verify appropriate errors

### Error Handling for User Story 3

- [ ] T032 [P] [US3] Create custom exceptions in src/main/java/com/yiava/exception/:
  - DocumentProcessingException.java
  - UnsupportedFormatException.java
  - CorruptedDocumentException.java
- [ ] T033 [P] [US3] Create ErrorResponse DTO at src/main/java/com/yiava/dto/ErrorResponse.java
- [ ] T034 [US3] Enhance GlobalExceptionHandler to map exceptions to HTTP responses
- [ ] T035 [US3] Add validation logic:
  - File extension check (DOC/DOCX only)
  - File size validation (<50MB)
  - Password-protected document detection
  - Corruption detection during parsing

### Controllers Enhancement for User Story 3

- [ ] T036 [US3] Enhance DocumentController with better error responses
- [ ] T037 [US3] Add validation annotations to DTOs and entity classes

### Tests for User Story 3

- [ ] T038 [P] [US3] Integration test for file format validation at src/test/java/yiava/controller/DocumentControllerTest.java
- [ ] T039 [P] [US3] Test password-protected document rejection
- [ ] T040 [P] [US3] Test corrupted file handling
- [ ] T041 [P] [US3] Test file size limit enforcement

**Checkpoint**: User Story 3 complete - Robust error handling with clear user feedback

---

## Phase 6: User Story 4 - Progress Tracking for Large Documents (Priority: P3)

**Goal**: Provide real-time progress updates during conversion for large documents (10+ pages)

**Independent Test**: Upload a 50+ page document and verify progress updates are received

### Progress Tracking for User Story 4

- [ ] T042 [P] [US4] Create ProgressService.java at src/main/java/com/yiava/service/ProgressService.java
  - Track conversion progress
  - Update percentage per page
  - Store progress in database
- [ ] T043 [P] [US4] Implement async conversion in DocumentConversionService using @Async
  - Return CompletableFuture<ConversionJob>
  - Update progress during conversion
  - Handle cancellation
- [ ] T044 [P] [US4] Create ProgressStatus DTO at src/main/java/com/yiava/dto/ProgressStatus.java

### Controllers for User Story 4

- [ ] T045 [US4] Add SSE endpoint for real-time progress in DocumentController.java
  - GET /api/conversion/stream/{jobId} (Server-Sent Events)
  - GET /api/conversion/{jobId}/status (Polling endpoint)
- [ ] T046 [US4] Configure Spring async executor in application.yml

### Tests for User Story 4

- [ ] T047 [P] [US4] Test async conversion with progress updates at src/test/java/yiava/service/DocumentConversionServiceTest.java
- [ ] T048 [P] [US4] Integration test for progress tracking endpoint

**Checkpoint**: User Story 4 complete - Real-time progress tracking for large documents

---

## Phase 7: User Story 5 - Query and Retrieve Conversion History (Priority: P2)

**Goal**: Query previously uploaded documents and download conversion results

**Independent Test**: Query document metadata and download previously converted files

### Query Services for User Story 5

- [ ] T049 [P] [US5] Implement DocumentQueryService.java at src/main/java/com/yiava/service/DocumentQueryService.java
  - Query documents by date range
  - Search by filename
  - Filter by status
  - Paginate results
- [ ] T050 [P] [US5] Create DocumentInfo DTO at src/main/java/com/yiava/dto/DocumentInfo.java
  - Document metadata with related jobs and files
  - Calculated fields (markdown file count, etc.)

### Query Controllers for User Story 5

- [ ] T051 [US5] Implement QueryController.java at src/main/java/com/yiava/controller/QueryController.java
  - GET /api/documents - List with pagination and filters
  - GET /api/documents/{id} - Get document details
  - GET /api/documents/{id}/files/{pageNumber} - Download specific markdown file
  - GET /api/documents/{id}/download - Download all files as ZIP

### Database Optimization for User Story 5

- [ ] T052 [US5] Add database indexes for query performance:
  - Index on document.upload_time
  - Index on document.status
  - Index on document.original_filename

### Tests for User Story 5

- [ ] T053 [P] [US5] Integration test for document query endpoint
- [ ] T054 [P] [US5] Test pagination and filtering
- [ ] T055 [P] [US5] Test file download functionality

**Checkpoint**: User Story 5 complete - Full query and retrieval capabilities

---

## Phase 8: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T056 [P] Add comprehensive integration tests at src/test/java/yiava/integration/
- [ ] T057 [P] Generate OpenAPI documentation using SpringDoc
- [ ] T058 [P] Add monitoring endpoints (Spring Boot Actuator)
- [ ] T059 Add performance optimization:
  - T059a Configure database connection pooling
  - T059b Add caching for frequent queries
  - T059c Optimize file I/O operations
- [ ] T060 [P] Security hardening:
  - T060a Add file upload validation
  - T060b Prevent path traversal attacks
  - T060c Add rate limiting (if needed)
- [ ] T061 [P] Add data retention cleanup job (90-day policy)
- [ ] T062 [P] Update README.md with setup and usage instructions
- [ ] T063 [P] Run mvn clean compile to verify project builds
- [ ] T064 [P] Run mvn test to verify all tests pass
- [ ] T065 [P] Validate against quickstart.md instructions

### Performance Testing Sub-Phase

- [ ] T066 [P] Add performance test for SC-001 at src/test/java/yiava/performance/ConversionPerformanceTest.java
  - Verify 10-page document conversion completes in under 30 seconds
- [ ] T067 [P] Add metric collection for SC-002 at src/test/java/yiava/performance/SuccessRateTest.java
  - Track conversion success rate over 100 document samples
- [ ] T068 [P] Add query performance test for SC-010 at src/test/java/yiava/performance/QueryPerformanceTest.java
  - Verify metadata queries complete in under 2 seconds
- [ ] T069 [P] Add page segmentation accuracy verification for SC-007 at src/test/java/yiava/performance/PageAccuracyTest.java
  - Verify 100% page boundary accuracy across various document types
- [ ] T070 [P] Add data retention verification for SC-009 at src/test/java/yiava/performance/DataRetentionTest.java
  - Verify files are accessible for 90 days and properly cleaned up afterward

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3-7)**: All depend on Foundational phase completion
  - Can proceed in parallel (if staffed) or sequentially by priority (P1 → P2 → P3)
- **Polish (Phase 8)**: Depends on desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational - Foundation for all other stories
- **User Story 2 (P1)**: Can start after Foundational - Builds on US1 infrastructure
- **User Story 3 (P2)**: Can start after Foundational - Error handling layer
- **User Story 4 (P3)**: Can start after Foundational - Progress tracking enhancement
- **User Story 5 (P2)**: Can start after Foundational - Query layer

### Within Each User Story

- DTOs and services (marked [P]) can run in parallel
- Services before controllers
- Controllers before integration tests
- Story complete before moving to next priority

### Parallel Opportunities

- All Setup tasks marked [P] can run in parallel
- All Foundational entity and mapper tasks (T010-T012) can run in parallel
- Once Foundational is complete, all user stories can start in parallel
- DTOs within each story marked [P] can run in parallel
- Different user stories can be worked on in parallel by different developers

---

## Parallel Example: User Story 1

```bash
# DTOs can be created in parallel:
Task: "Create UploadResponse DTO at src/main/java/com/yiava/dto/UploadResponse.java"
Task: "Create ConversionResult DTO at src/main/java/com/yiava/dto/ConversionResult.java"

# Services depend on DTOs and entities (must wait):
Task: "Implement DocumentConversionService.java"
Task: "Implement DocumentStorageService.java"

# Controllers depend on services:
Task: "Implement DocumentController.java"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL - blocks all stories)
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: Test User Story 1 independently - upload, convert, download
5. Deploy/demo if ready

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready
2. Add User Story 1 → Test independently → Deploy/Demo (MVP!)
3. Add User Story 2 → Test independently → Deploy/Demo
4. Add User Story 3 → Test independently → Deploy/Demo
5. Add User Story 4 → Test independently → Deploy/Demo
6. Add User Story 5 → Test independently → Deploy/Demo
7. Each story adds value without breaking previous stories

### Parallel Team Strategy

With multiple developers:

1. Team completes Setup + Foundational together
2. Once Foundational is done:
   - Developer A: User Story 1
   - Developer B: User Story 2
   - Developer C: User Story 3
3. Stories complete and integrate independently

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Test tasks marked with `src/test/` - write failing tests before implementation
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence

**Total Tasks**: 70 tasks across 8 phases
**User Story Task Count**:
- User Story 1 (P1): 8 tasks
- User Story 2 (P1): 8 tasks
- User Story 3 (P2): 10 tasks
- User Story 4 (P3): 7 tasks
- User Story 5 (P2): 7 tasks

**MVP Scope**: Phase 1 + Phase 2 + Phase 3 (User Story 1) = 23 tasks
