# College Management System

A Spring Boot REST API that models a college with **students, professors, subjects and admission records**. The project focuses on **JPA entity relationships** (OneToOne, OneToMany, ManyToOne, ManyToMany), including correct handling of the owning side and clean deletes.

## Tech Stack

- Java (version set in `pom.xml`)
- Spring Boot 4.1.0 (Spring Web MVC)
- Spring Data JPA / Hibernate
- MySQL
- Jakarta Bean Validation
- Lombok
- Spring Boot Actuator
- springdoc-openapi (Swagger UI)
- Maven

## Data Model

| Relationship | Type | Owning side | Notes |
|--------------|------|-------------|-------|
| Student ↔ Professor | ManyToMany | `Student` | Join table `student_professor` |
| Subject ↔ Student | ManyToMany | `Subject` | Join table `subject_student` |
| Subject → Professor | ManyToOne | `Subject` | Foreign key `professor_id` on `Subject` |
| AdmissionRecord → Student | OneToOne | `AdmissionRecord` | Unique foreign key `student_id`; removing a student cascades to its record |

Hibernate only writes the **owning side** of a relationship to the database, so the service layer always updates the owning side when linking entities.

## Features

- Layered architecture: Controller → Service → Repository, with DTOs so entities are never exposed
- All relationships lazily fetched
- **Clean deletes:** the service unlinks inverse-side associations before deleting a student or professor, so no foreign key violations or orphaned join rows
- One admission record per student, enforced in the service and by a database unique constraint
- Bean Validation on request bodies
- Custom exceptions with a global `@RestControllerAdvice` handler
- SLF4J logging, Actuator health and metrics endpoints, and Swagger UI

## API Endpoints

| Method | Endpoint | Description | Success |
|--------|----------|-------------|---------|
| POST | `/students` | Create a student | 201 |
| GET | `/students/{studentId}` | Get a student | 200 |
| DELETE | `/students/{studentId}` | Delete a student (and its admission record) | 204 |
| POST | `/students/professors` | Create a professor | 201 |
| GET | `/students/professors/{professorId}` | Get a professor | 200 |
| DELETE | `/students/professors/{professorId}` | Delete a professor | 204 |
| POST | `/students/subjects` | Create a subject | 201 |
| GET | `/students/subjects/{subjectId}` | Get a subject | 200 |
| DELETE | `/students/subjects/{subjectId}` | Delete a subject | 204 |
| POST | `/students/{studentId}/admission` | Create the student's admission record | 201 |
| PUT | `/students/{studentId}/professors/{professorId}` | Link a student to a professor | 200 |
| PUT | `/students/{studentId}/subjects/{subjectId}` | Link a subject to a student | 204 |

### Error handling

| Situation | Status |
|-----------|--------|
| Validation failed | 400 (with per-field `subErrors`) |
| Malformed JSON or invalid path value | 400 |
| Student / professor / subject not found | 404 |
| Duplicate admission record or data constraint violation | 409 |
| Anything unexpected | 500 (generic message, details logged) |

Example error response:

```json
{
  "timestamp": "2026-09-28T15:22:44",
  "status": 404,
  "message": "Student not found with id: 99"
}
```

## Running Locally

**Prerequisites:** JDK (see `pom.xml`), MySQL running locally.

1. Create the database:
   ```sql
   CREATE DATABASE IF NOT EXISTS college_management;
   ```
2. Set your database credentials as environment variables (nothing secret is stored in the repository):
   ```bash
   export DB_USERNAME=your_mysql_user
   export DB_PASSWORD=your_mysql_password
   ```
3. Start the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The app runs on `http://localhost:8080`.

| Tool | URL |
|------|-----|
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |
| Health check | `http://localhost:8080/actuator/health` |

## Known Limitations and Next Steps

- Professor and subject endpoints currently live under `/students`. They should move into separate controllers and services
- No update (PUT/PATCH) endpoints for the base entities yet
- No pagination
- No automated tests

## Author

Saheel Mohanta — [GitHub](https://github.com/humblefool2004)
