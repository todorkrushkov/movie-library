# Technical Documentation
## Movie Library REST API

---

## 1. Introduction

This document provides a technical overview of the Movie Library REST API.
It describes the architectural design, security model, asynchronous processing,
and key implementation decisions made during development.

The goal of the project is to demonstrate a clean, secure, and testable Spring Boot
application that manages a movie catalog and enriches movie data using an external API.

---

## 2. System Architecture

### Layers Overview

- **Controller Layer**
    - Exposes REST endpoints
    - Handles HTTP request/response mapping
    - Performs authorization checks via method-level security

- **Service Layer**
    - Contains business logic
    - Coordinates database operations
    - Triggers asynchronous rating enrichment

- **Repository Layer**
    - Uses Spring Data JPA
    - Encapsulates database access
    - Keeps persistence logic isolated

- **External Integration Layer**
    - Communicates with the OMDb API
    - Isolated behind service abstractions to allow easy replacement

This separation improves maintainability, testability, and extensibility.

---

## 3. Security Architecture

### 3.1 Authentication

Authentication is handled via Spring Security using a custom `UserDetailsService`.

- User credentials are loaded from the database
- Each user is associated with a role (`ADMIN` or `USER`)
- User identity is represented by a custom `UserDetails` implementation

The authentication process integrates seamlessly with Spring Security's filter chain.

---

### 3.2 Authorization

Authorization is enforced using **method-level security** with `@PreAuthorize`.

Authorization rules are centralized using a custom authorization helper, which provides:

- Authentication validation
- Admin-only access checks
- Owner-or-admin access checks

This approach avoids duplicated logic and keeps authorization rules explicit and testable.

#### Role Permissions

- **ADMIN**
    - Full CRUD access to movies
    - Full access to user management

- **USER**
    - Read-only access to movie data
    - Can access and modify only their own user information

---

## 4. Asynchronous Rating Enrichment

### 4.1 Motivation

Calling an external API during movie creation may introduce latency.
To prevent blocking client requests, rating enrichment is executed asynchronously.

---

### 4.2 Implementation

- When a movie is created, it is immediately saved to the database
- A background task queries the OMDb API using the movie title
- If a rating is found, it is applied and persisted separately

This ensures:

- Fast API responses
- Improved scalability
- Decoupling of external API latency from core application flow

---

### 4.3 Behavior Guarantees

- The POST `/movies` endpoint returns immediately
- Enrichment failures do not break movie creation
- The system remains responsive even if the external API is slow or unavailable

---

## 5. External API Integration

### OMDb API

The OMDb API is used to retrieve IMDb ratings.

- Queries are performed by movie title
- Only relevant data is extracted
- API access is configured via application properties

The integration is encapsulated in a dedicated client class to avoid tight coupling.

---

## 6. Error Handling

The application uses centralized exception handling.

- Domain-specific exceptions are mapped to appropriate HTTP status codes
- Error responses are consistent and predictable
- Validation and authorization failures are clearly communicated to clients

This approach improves API usability and debuggability.

---

## 7. Database Design

The database schema is intentionally simple and normalized.

- `users` table stores authentication and authorization data
- `movies` table stores movie metadata and ratings

Constraints such as unique titles and usernames enforce data integrity.

The schema supports the defined authorization model without unnecessary complexity.

---

## 8. Testing Strategy

The project includes comprehensive test coverage:

### Test Types

- **Unit Tests**
    - Services
    - Mappers
    - Validation helpers
    - Security helpers

- **Controller Tests**
    - REST endpoints
    - Authorization scenarios
    - Role-based access rules

### Security Testing

- ADMIN vs USER behavior is explicitly tested
- Ownership rules are validated
- Unauthorized access paths are covered

The testing strategy prioritizes correctness, security behavior, and regression safety.

---

## 9. Design Decisions & Trade-offs

### Explicit Authorization over Implicit Configuration
Authorization logic is kept explicit using method-level security rather than relying
solely on URL-based rules.

### Simplicity over Premature Optimization
The system avoids unnecessary complexity while remaining extensible.

### Testability as a First-Class Concern
All core components are designed to be testable in isolation.

---

## 10. Conclusion

****The Movie Library REST API demonstrates a secure, asynchronous, and well-structured Spring Boot application.****

****The design emphasizes clarity, correctness, and maintainability while meeting all functional and non-functional 
requirements defined in the project specification.****