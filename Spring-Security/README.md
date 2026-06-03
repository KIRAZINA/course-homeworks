# Spring-Security

A Spring Boot 3 application implementing user registration, secure authentication, and role-based access control. Developed using Spring Security 6.x, Spring Data JPA, and Thymeleaf.

## Features
- User registration with Jakarta Bean Validation
- Secure password hashing (BCrypt)
- Standard form-based login and logout
- Protected user list page (requires authentication)
- Automatic `ROLE_USER` assignment on registration
- Unit and integration tests (~90% coverage)

## Prerequisites
- Java 21
- Apache Maven 3.8+
- MySQL 8.x (via XAMPP or local installation)

## Database Setup
1. Start MySQL and create a database named `spring_security_db`.
2. Run the following DDL to create the required tables:
   ```sql
   CREATE TABLE IF NOT EXISTS users (id BIGINT AUTO_INCREMENT, email VARCHAR(255), phone VARCHAR(255), name VARCHAR(255), password VARCHAR(255), PRIMARY KEY (id));
   CREATE TABLE IF NOT EXISTS roles (id BIGINT AUTO_INCREMENT, name VARCHAR(255), PRIMARY KEY (id));
   CREATE TABLE IF NOT EXISTS user_roles (user_id BIGINT, role_id BIGINT);
   ```
3. Default credentials in `application.properties`: `root` / empty password / `localhost:3306`. Update if your setup differs.

## Running the Application
```bash
mvn clean spring-boot:run
```
Access the application at `http://localhost:8080/register`.

## Running Tests
Tests run against an in-memory H2 database and will not modify your MySQL data.
```bash
mvn clean test
```

## Project Structure
```
src/main/java/com/example/
├── config/          # SecurityFilterChain & password encoder
├── controller/      # Routing for login, registration, and user list
├── dto/             # Registration DTO with validation constraints
├── entity/          # JPA entities (User, Role) with ManyToMany mapping
├── repository/      # Spring Data JPA repositories
├── security/        # Custom UserDetailsService
└── service/         # Business logic for registration and user retrieval

src/main/resources/templates/  # Thymeleaf views
src/test/java/com/example/     # Unit, MVC, and integration tests
```

## Notes
- Passwords are encoded with `BCryptPasswordEncoder` before saving.
- CSRF protection is enabled by default. Thymeleaf's `th:action` automatically injects the CSRF token into forms.
- Integration tests use `@ActiveProfiles("test")` to override MySQL with H2.