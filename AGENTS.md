# AGENTS.md - University Portal Project Instructions

## Project Purpose
A Java CLI-based University Portal system managing students, teachers, courses, and enrollments. 

## Architecture
- `Model/`: Domain entities (User, Course, Enrollment).
- `Repositories/`: In-memory data storage and serialization (`UniversityDataStore`).
- `Service/`: Business logic (Auth, Enrollment, Marks).
- `Main/`: CLI routing (`MainMenuUI`, `ApplicationContext`).


## Coding Conventions
- Java 17+.
- Prefer interfaces over concrete implementations where applicable.
- Use standard Java naming conventions (PascalCase for classes, camelCase for methods).
- Do not use external frameworks (Spring, Hibernate); this is a pure Java OOP project.

## Test Expectations
- Any new feature or refactoring must be accompanied by JUnit 5 tests in `src/test/java/`.
- Run `mvn test` before proposing any changes.

## Forbidden Actions
- **No Secrets:** Never commit passwords, API keys, or real personal data.
- **No Destructive Commands:** Do not run `rm -rf`, `git reset --hard`, or `git push --force`.
- **No Unreviewed Dependencies:** Do not modify `pom.xml` without explicit human approval.
- **No Direct DB Changes:** The system uses `.ser` file serialization; do not delete or modify `data/university.ser` without approval.