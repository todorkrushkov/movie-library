<div align="center">

<img src="https://readme-typing-svg.herokuapp.com?font=Fira+Code&weight=700&size=35&pause=1000&color=6DB33F&center=true&vCenter=true&width=600&lines=Movie+Library+REST+API;Secure+Spring+Boot+Backend;Asynchronous+OMDb+Enrichment;Role-Based+Access+Control" alt="Typing SVG" />

[![Java](https://img.shields.io/badge/Java-17-555555?style=for-the-badge&logo=openjdk&logoColor=white&labelColor=ED8B00)](#)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.7-555555?style=for-the-badge&logo=springboot&logoColor=white&labelColor=6DB33F)](#)
[![Gradle](https://img.shields.io/badge/Gradle-8.5-555555?style=for-the-badge&logo=gradle&logoColor=white&labelColor=02303A)](#)
[![MariaDB](https://img.shields.io/badge/MariaDB-10.11-555555?style=for-the-badge&logo=mariadb&logoColor=white&labelColor=003545)](#)
[![JWT](https://img.shields.io/badge/JWT-0.11.5-555555?style=for-the-badge&logo=jsonwebtokens&logoColor=white&labelColor=000000)](#)
[![Swagger](https://img.shields.io/badge/Swagger-3.0-555555?style=for-the-badge&logo=swagger&logoColor=black&labelColor=85EA2D)](#)
[![OpenAPI](https://img.shields.io/badge/OpenAPI-3.0-555555?style=for-the-badge&logo=openapiinitiative&logoColor=white&labelColor=6BA539)](#)

A **secure, production-style Spring Boot REST API** for managing a movie catalog with users, roles, watched movies, and **asynchronous enrichment from the OMDb API**.

---
</div>

## Core Features

### Movie Management
- CRUD operations for movies (**ADMIN only**)
- Public movie search with advanced filters:
    - Partial title match
    - Director
    - Release date range
    - IMDb rating range
- **Asynchronous enrichment** using OMDb API:
    - IMDb rating
    - Director
    - Release date
- Validation against duplicate movie titles

### User & Access Management
- Secure user registration, login, and logout
- Role-based authorization (`USER`, `ADMIN`)
- Ownership-based access control for user resources
- Dev profile data seeding for faster local testing

### Watched Movies
- Per-user watched movie tracking
- Notes attached to watched movies
- Duplicate watched movie prevention
- Automatic watched date tracking

---

## Technical Highlights

### JWT Authentication via HTTP-only Cookies
- JWT tokens are generated on login/registration
- Stored in **HTTP-only cookies** (not local storage)
- Custom `JwtAuthenticationFilter`:
    - Extracts JWT from cookies
    - Validates token integrity & expiration
    - Populates Spring Security context
- Protects against XSS-based token theft

---

### Unified API Response Contract
All API responses follow a **single response schema**:

```json
{
  "success": true,
  "status": 200,
  "path": null,
  "message": "Operation successful.",
  "data": {},
  "errors": null,
  "timestamp": "2026-01-01T12:00:00"
}
```

## Benefits
- Predictable API behavior
- Easy frontend integration
- Centralized error handling

---

## Centralized Global Exception Handling

Implemented using `@RestControllerAdvice`.

**Handled exceptions**
- `EntityNotFoundException`
- `EntityDuplicateException`
- `AuthenticationFailureException`
- `AuthorizationFailureException`
- `ExternalApiServiceException`
- Validation & framework exceptions

**HTTP status mapping**
- `401` – Authentication failure
- `403` – Authorization failure
- `404` – Resource not found
- `409` – Duplicate entity
- `422` – Validation errors
- `500` – Unexpected server errors

---

## Method-Level Authorization & Ownership Validation
- Uses `@PreAuthorize`
- Custom authorization helper:

```java
@PreAuthorize("hasRole('ADMIN') or @authorize.isOwner(#userId)")
```

- Enforces resource ownership
- Keeps authorization logic out of business services

---

## Asynchronous External API Integration
- OMDb enrichment runs in the background using `@Async`
- Movie creation remains **non-blocking**
- Fault-tolerant handling of:
    - Client errors
    - Server errors
    - Network timeouts
- Movies are enriched **after persistence** for data consistency

---

## DTO & Mapper-Based Design
- Entities are never exposed directly
- Dedicated mapper classes:
    - `MovieMapper`
    - `UserMapper`
    - `WatchedMovieMapper`
    - `OmdbMapper`
- Supports partial updates and clean API contracts

---

## Security Hardening
- Stateless security with JWT
- CSRF disabled (cookie-based JWT)
- Custom handlers:
    - `CustomAuthenticationEntryPoint`
    - `CustomAccessDeniedHandler`
- Clear separation between authentication and authorization

---

## Validation Strategy
- Jakarta Bean Validation (`@NotBlank`, `@Size`)
- Validation failures return:
    - Field-level error messages
    - HTTP `422 Unprocessable Entity`
- Business rule validation separated into helper classes

---

## Clean Layered Architecture

```text
  Controller  →  Service  →  Repository
    ↓    ↑        ↓   ↑
   DTO Entity   Validation
    ↓    ↑
    Mapper
   ```

- Business logic isolated in services
- Reusable validation helpers
- Repositories limited to data access

---

## Database & Persistence Design
- MariaDB with JPA/Hibernate
- Custom JPQL search queries
- Lazy-loaded relations
- Explicit transactional boundaries
- Identity-based entity equality

---

## API Endpoints (Summary)

### Authentication

```text
POST   /api/auth/register
POST   /api/auth/login
POST   /api/auth/logout
```

### Movies

```text
GET    /api/movies
GET    /api/movies/{id}
POST   /api/movies           (ADMIN)
PUT    /api/movies/{id}      (ADMIN)
DELETE /api/movies/{id}      (ADMIN)
```

### Users

```text
GET    /api/users             (ADMIN)
GET    /api/users/{id}
PUT    /api/users/{id}
DELETE /api/users/{id}
```

### Watched Movies

```text
GET    /api/users/{userId}/watched
POST   /api/users/{userId}/watched/{movieId}
PUT    /api/users/{userId}/watched/{movieId}
```

---

## Development Profile

When running with the dev profile, the application auto-seeds users:

| Username | Password | Role |
|----------|----------|------|
| admin    | admin12  | ADMIN |
| user     | 12345678 | USER |

---

## Configuration

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/movie_library
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password

omdb.api.key=YOUR_OMDB_API_KEY
```

## Running the Application

```bash
  ./gradlew bootRun
```

## Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

---

## Future Enhancements
- Refresh tokens
- Rate limiting
- OMDb response caching
- Integration and contract testing

---

## Contributors

For further information, questions, or feedback, feel free to get in touch:

| Name           | Email                        | GitHub                           |
|----------------|------------------------------|----------------------------------|
| Todor Krushkov | todorkrushkov.1304@gmail.com | https://github.com/todorkrushkov |

---

## Notes
- This project was developed as part of the **Java Alpha program at Telerik Academy**.
- The application is designed to prioritize clarity, correctness, and testability
- Security and asynchronous behavior are implemented explicitly rather than implicitly


Developed as part of a Telerik Academy backend engineering project.

Built with clean code, security, and scalability in mind.