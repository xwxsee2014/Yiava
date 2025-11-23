# Tasks: Spring Boot CRUD Init

**Input**: Design documents from `specs/001-spring-boot-crud/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: The examples below include test tasks. Tests are OPTIONAL - only include them if explicitly requested in the feature specification.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Single project**: `src/`, `tests/` at repository root
- **Web app**: `backend/src/`, `frontend/src/`
- **Mobile**: `api/src/`, `ios/src/` or `android/src/`
- Paths shown below assume single project - adjust based on plan.md structure

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [x] T001 Create project structure per implementation plan (src/main/java/com/yiava/, src/test/, src/main/resources/)
- [x] T002 Initialize pom.xml with Spring Boot 2.7, MyBatis, Flyway, Actuator, Druid, Logback dependencies
- [x] T003 [P] Configure Maven compiler plugin for Java 17 and resource filtering

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [x] T004 Create application.yml with database, Druid, MyBatis, and Actuator configuration
- [x] T005 Create logback.xml with console and file appenders, rolling policy, and structured logging
- [x] T006 Create Flyway migration script V1__Create_content_table.sql in src/main/resources/db/migration/
- [x] T007 [P] Create Content entity class in src/main/java/com/yiava/entity/Content.java
- [x] T008 [P] Create ContentRequest DTO in src/main/java/com/yiava/dto/ContentRequest.java
- [x] T009 [P] Create ContentResponse record in src/main/java/com/yiava/dto/ContentResponse.java
- [x] T010 [P] Create MyBatis configuration class with @MapperScan in src/main/java/com/yiava/config/MyBatisConfig.java
- [x] T011 [P] Create global exception handler with @ControllerAdvice in src/main/java/com/yiava/config/GlobalExceptionHandler.java
- [x] T012 Create main application class YiavaApplication.java in src/main/java/com/yiava/

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Create Content Records (Priority: P1) 🎯 MVP

**Goal**: API consumers can create new content records via POST /api/content

**Independent Test**: POST to /api/content with valid JSON and verify record is created with unique ID

### Implementation for User Story 1

- [x] T013 [P] [US1] Create ContentMapper interface in src/main/java/com/yiava/mapper/ContentMapper.java
- [x] T014 [P] [US1] Create ContentMapper XML with insert method in src/main/resources/mapper/ContentMapper.xml
- [x] T015 [US1] Implement ContentService with create method in src/main/java/com/yiava/service/ContentService.java
- [x] T016 [US1] Implement ContentController with POST /api/content endpoint in src/main/java/com/yiava/controller/ContentController.java
- [x] T017 [US1] Add validation for ContentRequest (not blank, max length 5000)
- [x] T018 [US1] Add logging for create operations using SLF4J

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Read Content Records (Priority: P1)

**Goal**: API consumers can retrieve content records via GET /api/content and GET /api/content/{id}

**Independent Test**: GET requests to /api/content (list) and /api/content/{id} (detail) return correct data

### Implementation for User Story 2

- [ ] T019 [P] [US2] Add selectAll and selectById methods to ContentMapper interface
- [ ] T020 [P] [US2] Add select SQL queries to ContentMapper.xml
- [ ] T021 [US2] Implement ContentService with findAll and findById methods
- [ ] T022 [US2] Implement ContentController with GET /api/content and GET /api/content/{id} endpoints
- [ ] T023 [US2] Add @NotFoundException for missing records
- [ ] T024 [US2] Add logging for read operations

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: User Story 3 - Update Content Records (Priority: P2)

**Goal**: API consumers can modify existing content records via PUT /api/content/{id}

**Independent Test**: PUT requests to /api/content/{id} with updated data and verify changes persist

### Implementation for User Story 3

- [ ] T025 [P] [US3] Add update method to ContentMapper interface
- [ ] T026 [P] [US3] Add update SQL query to ContentMapper.xml
- [ ] T027 [US3] Implement ContentService with update method
- [ ] T028 [US3] Implement ContentController with PUT /api/content/{id} endpoint
- [ ] T029 [US3] Add optimistic locking check using updated_at timestamp
- [ ] T030 [US3] Add logging for update operations

**Checkpoint**: User Stories 1, 2, and 3 should now work independently

---

## Phase 6: User Story 4 - Delete Content Records (Priority: P3)

**Goal**: API consumers can remove content records via DELETE /api/content/{id}

**Independent Test**: DELETE requests to /api/content/{id} and verify record is removed

### Implementation for User Story 4

- [ ] T031 [P] [US4] Add delete method to ContentMapper interface
- [ ] T032 [P] [US4] Add delete SQL query to ContentMapper.xml
- [ ] T033 [US4] Implement ContentService with delete method
- [ ] T034 [US4] Implement ContentController with DELETE /api/content/{id} endpoint
- [ ] T035 [US4] Add logging for delete operations

**Checkpoint**: All user stories should now be independently functional

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T036 [P] Add SpringDoc OpenAPI dependency and configuration for API documentation
- [ ] T037 [P] Create comprehensive unit tests for ContentService using JUnit 5 and Mockito
- [ ] T038 [P] Create integration tests with @SpringBootTest for full stack testing
- [ ] T039 Add Druid monitoring configuration and security (disable in production)
- [ ] T040 Add health check indicators for database connectivity in Actuator
- [ ] T041 Add custom metrics for CRUD operation counts and timing
- [ ] T042 [P] Create README.md with build and run instructions in repository root
- [ ] T043 [P] Add @Transactional annotations to service methods with proper isolation levels
- [ ] T044 Review and optimize SQL queries based on execution plans
- [ ] T045 [P] Add configuration profiles for dev, test, and prod environments

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3-6)**: All depend on Foundational phase completion
  - User stories can then proceed in parallel (if staffed)
  - Or sequentially in priority order (P1 → P2 → P3)
- **Polish (Final Phase)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 2 (P1)**: Can start after Foundational (Phase 2) - Shares Content entity and mapper but independently testable
- **User Story 3 (P2)**: Can start after Foundational (Phase 2) - Builds on Content entity, independently testable
- **User Story 4 (P3)**: Can start after Foundational (Phase 2) - Uses same entities, independently testable

### Within Each User Story

- Mappers (interface + XML) before services
- Services before controllers
- Validation before implementation
- Logging added after core functionality
- Story complete before moving to next priority

### Parallel Opportunities

- All Setup tasks marked [P] can run in parallel
- All Foundational tasks marked [P] can run in parallel (within Phase 2)
- Once Foundational phase completes, all user stories can start in parallel (if team capacity allows)
- All mappers within a story marked [P] can run in parallel
- Different user stories can be worked on in parallel by different team members
- All polish tasks marked [P] can run in parallel

### File Path Conflicts to Avoid

- ContentMapper.java and ContentMapper.xml are separate files (can parallel)
- DTOs (ContentRequest, ContentResponse) are separate files (can parallel)
- All entities are in same package but different files (can parallel)
- Controllers, Services, Mappers are in different packages (can parallel)
- Test files are in separate test directories (can parallel)

---

## Parallel Example: User Story 1

```bash
# Tasks that can run in parallel for User Story 1:
Task: T013 [P] [US1] Create ContentMapper interface
Task: T014 [P] [US1] Create ContentMapper XML

# These two can be worked on simultaneously as they define the same interface
# After both complete, proceed to:
Task: T015 [US1] Implement ContentService
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL - blocks all stories)
3. Complete Phase 3: User Story 1 (Create)
4. **STOP and VALIDATE**: Test create operation independently
5. Deploy/demo if ready - you now have a working CRUD foundation

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready
2. Add User Story 1 → Test independently → Deploy/Demo (MVP!)
3. Add User Story 2 → Test independently → Deploy/Demo
4. Add User Story 3 → Test independently → Deploy/Demo
5. Add User Story 4 → Test independently → Deploy/Demo
6. Each story adds value without breaking previous stories

### Parallel Team Strategy

With multiple developers:

1. Team completes Setup + Foundational together
2. Once Foundational is done:
   - Developer A: User Story 1 (Create)
   - Developer B: User Story 2 (Read)
   - Developer C: User Story 3 (Update)
3. Once US1, US2, US3 are done:
   - Developer D: User Story 4 (Delete)
   - Or distribute polish tasks
4. Stories complete and integrate independently

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Verify tests fail before implementing
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence
