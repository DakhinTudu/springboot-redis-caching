# Spring Boot Redis Caching Demo

A modern, high-performance reference application built with **Spring Boot**, **Spring Cache**, **Redis**, **Spring Data JPA**, and **MapStruct**, demonstrating best practices for distributed caching, automatic auditing, robust validation, custom exception handling, and standardized client responses.

---

## 🚀 Key Features

* **Distributed Caching with Redis**: Full integration of Spring's cache abstraction (`@Cacheable`, `@CacheEvict`, `@Caching`) using Redis as the cache provider.
* **Custom JSON Serialization**: Configures Redis templates to serialize/deserialize payload data structured in standard Jackson-based JSON instead of default Java serialization binary formats for transparency and cross-system compatibility.
* **MapStruct DTO Mapping**: Type-safe, high-performance compilation-time object mapping between database Entities (`Books`) and API DTO Records (`BookRequest`, `BookResponse`, `BookUpdateRequest`).
* **Lombok & MapStruct Integration**: Seamless configuration handling constructor-based mapper injection and class-level unmapped target checks.
* **Robust Request Validation**: Active annotation-based request validation (`@Valid` validation rules) verifying client inputs on book creation and updates.
* **JPA Entity Auditing**: Extends a reusable `Auditable` superclass mapping `createdAt`, `createdBy`, `updatedAt`, and `updatedBy` properties automatically via JPA Entity Listeners.
* **Custom Exception Handling**: Clean exception architecture featuring `BookNotFoundException` returning precise API messages.
* **Systematic Cache Invalidation (Eviction Patterns)**:
  * When a book is created, the paginated lists (`page-books`) cache is fully invalidated.
  * When a book is updated or deleted, both its individual cache (`books::bookId`) and the list queries (`page-books`) caches are evicted automatically to prevent stale reads.
* **Standardized API Response wrapping**: Unified response wrapper API payload structure (`ApiResponse<T>`) including HTTP status details, ISO timestamps, and detailed page metas.
* **Active Data Seeding**: Automatically populates sample books upon first environment startup via `CommandLineRunner` if empty.
* **Auto-generated Swagger/OpenAPI Docs**: Integrated interactive developer playground utilizing Springdoc-OpenAPI.

---

## 🛠️ Tech Stack & Requirements

* **Language**: Java 21
* **Framework**: Spring Boot 4.1.0-SNAPSHOT (or latest parent starter)
* **Object Mapper**: MapStruct 1.5.5.Final
* **Database**: In-Memory H2 Database with JPA Auditing
* **Cache Provider**: Redis
* **Documentation**: Springdoc-OpenAPI v3
* **Build Tool**: Maven

---

## 📁 Repository Structure

```text
src/main/java/com/redisdemo
├── config
│   ├── DataInitializer.java        # Seeds H2 DB on startup if empty
│   └── RedisConfig.java            # Tweaks serialization & TTLs (10 min duration)
├── controller
│   └── BookController.java         # REST endpoints for CRUD actions with @Valid validation
├── dto                             # Record DTO Layer
│   ├── ApiResponse.java            # Master API response wrapper
│   ├── Auditable.java              # JPA Auditing base abstract class
│   ├── BookRequest.java            # Book creation constraints record
│   ├── BookResponse.java           # Book output information record
│   ├── BookUpdateRequest.java      # Book modification constraints record
│   └── PaginationMeta.java         # Standard pagination properties metadata
├── entity
│   └── Books.java                  # JPA Entity mapping utilizing UUIDs, extends Auditable
├── exception
│   └── BookNotFoundException.java  # Custom domain exception throwing on resource miss
├── mapper
│   └── BookMapper.java             # MapStruct interface compiling to mapper implementations
├── repository
│   └── BookRepository.java         # Database Repository extends JpaRepository
├── service
│   ├── BookService.java            # Service contract interface returning DTOs
│   └── impl
│       └── BookServiceImpl.java    # Service Implementation injecting MapStruct & repositories
└── utils
    ├── ApiResponseBuilder.java     # Response building utility helper
    └── PaginationMapper.java       # Maps Page metadata to standard API metadata
```

---

## ⚙️ Setup & Execution

### 1. Prerequisite: Run Redis
Ensure Redis is running locally. You can quickly spin up a Redis server using Docker:
```bash
docker run --name local-redis -p 6379:6379 -d redis
```

### 2. Configure Settings (Optional)
Check the `src/main/resources/application.properties` settings:
```properties
spring.application.name=redis-demo
spring.data.rest.basePath=/api

spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=password
spring.datasource.driverClassName=org.h2.Driver
spring.h2.console.enabled=true

spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```

### 3. Run the Application
Use the Maven wrapper to build and start the project locally:

**On Windows (PowerShell/CMD):**
```powershell
.\mvnw.cmd clean spring-boot:run
```

**On Linux/macOS:**
```bash
chmod +x mvnw
./mvnw clean spring-boot:run
```

Once up, the console will confirm:
`Samples books inserted into the database successfully!`

---

## 🎯 REST API Endpoints

### 1. Fetch Paginated Books (Cached)
Retrieves a page list of books. Results are cached inside Redis under key `page-books::<page>-<size>` with a TTL of 10 minutes.
* **Route**: `GET /api/books`
* **Query Params**: `page` (default `0`), `size` (default `10`)
* **Response Header Cache Check**: Look at your local Redis server using CLI (`KEYS *`) to notice `page-books::0-10`.
* **Example JSON Response**:
```json
{
  "success": true,
  "message": "Books fetched successfully",
  "data": [
    {
      "bookId": "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d",
      "bookName": "The Hobbit",
      "authorName": "J.R.R. Tolkien"
    }
  ],
  "pagination": {
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 5,
    "totalPages": 1,
    "first": true,
    "last": true,
    "hasNext": false,
    "hasPrevious": false
  },
  "status": 200,
  "timestamp": "2026-06-29T12:00:00Z"
}
```

### 2. Retrieve Book by UUID (Cached)
Retrieves a single book. Cached in Redis under key `books::<bookId>`.
* **Route**: `GET /api/books/{bookId}`

### 3. Create a Book
Inserts a new book into the database and evicts the list caching (`allEntries = true` under region `page-books`).
* **Route**: `POST /api/books`
* **Sample Payload**:
```json
{
  "bookName": "Clean Code",
  "authorName": "Robert C. Martin"
}
```

### 4. Update a Book
Updates the entity dynamically using DTO properties matching via MapStruct and evicts both single-book cache (`books::bookId`) and pagination caches (`page-books`).
* **Route**: `PUT /api/books/{bookId}`
* **Payload**:
```json
{
  "bookName": "Clean Code (Revised Edition)",
  "authorName": "Robert C. Martin"
}
```

### 5. Delete a Book
Removes the entity from DB and cascades cache eviction tags.
* **Route**: `DELETE /api/books/{bookId}`

---

## 🛠️ Debugging & Inspection tools

### H2 DB Console
* **Console URL**: `http://localhost:8080/h2-console`
* **JDBC URL**: `jdbc:h2:mem:testdb`
* **Username**: `sa`
* **Password**: `password`

### Swagger UI Docs
Explore all API details visually using Swagger:
* **Interactive Docs**: `http://localhost:8080/swagger-ui/index.html`
* **OpenAPI Specs**: `http://localhost:8080/v3/api-docs`

---
