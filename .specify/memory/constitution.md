<!--
Sync Impact Report - Constitution Update
========================================

Version change: N/A → 1.0.0
Version bump type: MAJOR (initial ratification)
Ratification date: 2025-11-21

New Core Principles Added (I-VI):
- I. Research-First Development: Emphasizes documented experiments with clear goals
- II. Java Best Practices: Modern Java 17 patterns (records, sealed classes, Optional, streams)
- III. Layered Architecture: Controller/Service/Mapper separation (NON-NEGOTIABLE)
- IV. Testing Discipline: Unit, integration, and contract testing requirements
- V. Documentation & Knowledge Sharing: CLAUDE.md as living documentation
- VI. Simplicity & YAGNI: Avoid speculative design, justify complexity

New Sections Added:
- Technology Standards: JDK 17, Spring Boot 2.7, MyBatis, Maven specifications
- Development Workflow: Branching, Maven commands, PR requirements, code review process
- Governance: Amendment procedures, version tracking, compliance verification

Removed Sections: N/A (initial creation)

Templates Updated:
✅ .specify/templates/plan-template.md - Updated Constitution Check with 6 concrete gates
✅ .specify/templates/spec-template.md - No changes required (generic)
✅ .specify/templates/tasks-template.md - No changes required (generic)
✅ .specify/templates/checklist-template.md - No changes required (generic)

Dependent Templates Status:
✅ .claude/commands/speckit.analyze.md - Already references constitution (no changes needed)
✅ .claude/commands/speckit.plan.md - Already references constitution (no changes needed)

Follow-up TODOs: None

Validation Results:
✅ No remaining bracket tokens
✅ Version line matches report
✅ Dates in ISO format (2025-11-21)
✅ All principles use declarative language with MUST/SHOULD rationale
✅ Architecture alignment verified across plan-template
-->

# Yiava Constitution

## Core Principles

### I. Research-First Development
Every experiment MUST be documented with purpose, expected outcomes, and learnings. New features start as isolated research modules. Clear research questions required before implementation - no experimentation without stated learning goals. Results and insights MUST be captured for future reference.

### II. Java Best Practices
Follow modern Java 17 patterns and idioms: records for data carriers, sealed classes for controlled inheritance, pattern matching where applicable, Optional for null-safety, streams for data processing. Use Lombok judiciously and only with explicit justification. Prefer immutability and functional programming patterns.

### III. Layered Architecture (NON-NEGOTIABLE)
Controllers handle HTTP concerns only - no business logic in controller layer. Services contain business logic, workflows, and transaction management. Mappers handle SQL and data persistence. DTOs separate API contracts from entities. Clear boundaries between layers MUST be maintained to prevent coupling.

### IV. Testing Discipline
Unit tests mandatory for all business logic in services. Integration tests required for MyBatis mappers and database interactions. Test structure mirrors source: tests/unit/, tests/integration/, tests/contract/ following Maven conventions. Property-based testing encouraged for complex domain logic.

### V. Documentation & Knowledge Sharing
CLAUDE.md serves as living documentation for project conventions. All experiments MUST include README or inline documentation. Code comments explain complex business logic and non-obvious decisions. API documentation via SpringDoc/OpenAPI when REST endpoints added. Research findings documented in feature branches before merging.

### VI. Simplicity & YAGNI
Start simple - implement minimum viable solution first. Avoid premature generalization and speculative design. Complexity MUST be justified with concrete evidence of need. Refactor based on actual requirements, not anticipated ones. Prefer composability over deep inheritance hierarchies.

## Technology Standards

JDK 17 (LTS) is required for all Java code. Spring Boot 2.7 series for application framework - latest 2.x patch version. MyBatis for SQL mapping with XML-based queries preferred over annotations. Maven for build management - no Gradle. Database migrations via Flyway when schema evolution needed. Standard Maven directory structure enforced for all modules.

## Development Workflow

Use feature branches with descriptive names. Maven commands: `mvn clean compile` for builds, `mvn test` for testing, `mvn spring-boot:run` for local development. Commit messages follow conventional format: type(scope): description. Pull requests required for main branch changes. Code review verifies architecture compliance and test coverage. Hot reload via Spring Boot DevTools enabled for development.

## Governance

Constitution supersedes all other development practices. Amendments require rationale, impact assessment, and migration plan for affected components. Version tracking follows semantic versioning for constitution changes: MAJOR for principle removals/redefinitions, MINOR for new principles, PATCH for clarifications. Compliance verified during code review - violations must be explicitly justified in Complexity Tracking section of implementation plans. Use CLAUDE.md for runtime development guidance and command references.

**Version**: 1.0.0 | **Ratified**: 2025-11-21 | **Last Amended**: 2025-11-21
