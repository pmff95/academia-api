# Demo Spring Boot API

[Leia em Português](README.md)

This project is a multi-feature REST API built with Spring Boot. It is split into several packages which group together related controllers, services and domain models.

## Prerequisites

- **Java 23** (or compatible JDK)
- **Maven 3.8+** (the repository also provides the Maven Wrapper `mvnw`)

## Building the Project

Use Maven to resolve dependencies and package the application:

```bash
./mvnw clean package
```

The above command produces `target/demo-0.0.1-SNAPSHOT.jar`.

## Running the Application

You can start the application directly from Maven or by executing the packaged jar:

```bash
# Run using the Maven Spring Boot plugin
./mvnw spring-boot:run

# Or run the packaged jar
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## Environment Variables

The application expects the following variables:

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`
- `DB_SCHEME`
- `DB_AUDIT_SCHEME`
- `API_KEY`
- `MAIL_FROM`
- `MAIL_HOST`
- `MAIL_PORT`
- `MAIL_USERNAME`
- `MAIL_PASSWORD`
- `CLOUDFLARE_R2_ACCESS_KEY`
- `CLOUDFLARE_R2_SECRET_KEY`
- `CLOUDFLARE_R2_ENDPOINT`
- `CLOUDFLARE_R2_PUBLIC_DOMAIN`
- `CLOUDFLARE_R2_BUCKET`
- `JWT_SECRET`
- `JWT_EXPIRATION`
- `JWT_REFRESH_EXPIRATION`

Set `SPRING_PROFILES_ACTIVE=dev` to load values automatically from `.env`.
Example usage in `application.properties`:

```properties
db.username=${DB_USER}
db.password=${DB_PASSWORD}
api.key=${API_KEY}
```
Create a `.env` file with these keys. An example template is provided in
`.env.example`.

## Project Modules

The source code under `src/main/java/com/example/demo` is organised by feature. Below is a brief overview of the main packages:

- **common** – shared configuration, utilities, security and validation helpers.
- **controller** – REST controllers exposing API endpoints for each feature module.
- **domain** – JPA entities and related model classes.
- **dto** – Data Transfer Objects used by the controllers and services.
- **repository** – Spring Data repositories for persistence operations.
- **service** – business services implementing the application logic.

Feature‑specific packages include `aluno`, `professor`, `usuario`, `carteira`, `instituicao`, `vendas` and others. Each contains its own controllers, services and repositories for that domain.

