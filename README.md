# Appointment Service

Service responsible for creating and updating hospital appointments. It validates participants against the User Service (gRPC with mTLS), persists data in PostgreSQL, and publishes appointment events to RabbitMQ (fanout exchange) so other services (Notification Service, Appointment History Service) can react asynchronously.

Part of the hospital_app microservices system.


## Tech stack
- Java 21, Spring Boot 3.5
- Spring Web, Validation, Security (JWT resource server via shared jwt_security_common module)
- Spring Data JPA (PostgreSQL)
- springdoc-openapi (Swagger UI)
- gRPC client (net.devh) with mutual TLS to User Service
- RabbitMQ with TLS (JSON messages)


## Architecture highlights
- Hexagonal architecture (ports and adapters)
  - inbound adapter: REST controller (create/update appointment)
  - outbound adapters:
    - gRPC client to User Service to validate users and retrieve user info
    - RabbitMQ producer to publish AppointmentMessage to fanout exchange
    - JPA repository for persistence
- Message exchange: Fanout exchange appointment.exchange; consumers bind their queues to receive events.


## Running locally
There are two common ways to run this service.

1) Using Docker Compose (recommended to run the whole system)
- Prerequisites: Docker and Docker Compose
- From the repo root, copy .env as needed and run:
  docker compose up -d appointment-service appointment-service-db rabbitmq
- The service will be available on: http://localhost:8082
- Swagger UI: http://localhost:8082/swagger-ui/index.html
- PostgreSQL (service DB): localhost:5435 (mapped to container 5432)

Note: appointment-service depends on the User Service (for gRPC). For full functionality (user validation) you should also run user-service and its DB:
  docker compose up -d user-service user-service-db

2) Running via Maven locally
- Prerequisites: JDK 21, Maven, a running PostgreSQL and RabbitMQ with TLS, and the User Service for gRPC
- Export the same env vars as in the .env file (see Environment variables section) or create an application-local.yml
- From the repo root (monorepo builds shared modules), run:
  mvn -q -DskipTests package
  mvn -q -pl appointment_service -am spring-boot:run
- Service runs on port 8080 by default; in Docker it is mapped to 8082.

Remote debugging
- If ENABLE_REMOTE_DEBUG=true in Docker, Java debug port 5006 is exposed.


## API
Base path: /appointment

- POST /appointment
  - Description: Create a new appointment
  - Auth: Bearer JWT; required roles: NURSE, DOCTOR, ADMIN
  - Request body example:
    {
      "patientId": "11111111-1111-1111-1111-111111111111",
      "doctorId": "22222222-2222-2222-2222-222222222222",
      "date": "2025-09-10T10:00:00Z",
      "reason": "Annual check"
    }
  - 201 Created with body AppointmentResponseDto

- PUT /appointment/{id}
  - Description: Update an existing appointment by ID
  - Auth: Bearer JWT; required roles: NURSE, DOCTOR, ADMIN
  - Request body example:
    {
      "date": "2025-09-10T11:00:00Z",
      "reason": "Rescheduled"
    }
  - 200 OK with body AppointmentResponseDto

Additional security rules in code also include:
- GET /appointment/mine allowed to roles PATIENT, ADMIN
- GET /appointment allowed to roles NURSE, DOCTOR, ADMIN (if implemented in the current version)

OpenAPI/Swagger
- Swagger UI: /swagger-ui/index.html
- OpenAPI JSON: /v3/api-docs


## Messaging
- Exchange: appointment.exchange (fanout)
- Producer: publishes com.hospital_app.common.message.dto.AppointmentMessage as JSON
- Consumers:
  - Notification Service: sends email notifications
  - Appointment History Service: stores event history

RabbitMQ TLS settings are read from environment (keystore/truststore). Ensure RabbitMQ is running with the certs provided in the repo (see rabbitmq/).


## gRPC to User Service
- Host: user-service
- Port: 9090
- mTLS using certificates in classpath tls/ directory:
  - appointment_service.crt
  - appointment_service_pkcs8.key
  - ca.crt
- The adapter is at infra/adapter/out/user_service/grpc/UserServiceClientGrpcAdapter.java


## Persistence
- PostgreSQL via Spring Data JPA
- Schema is created/updated according to HOSPITAL_APP_APPOINTMENT_SERVICE_DB_DDL_AUTO (default update in .env).


## Environment variables
These are consumed by appointment_service (see application.yml and docker-compose.yml). Values below reflect defaults from the root .env file; adjust for your environment.

Database
- HOSPITAL_APP_APPOINTMENT_SERVICE_DB_URL=jdbc:postgresql://appointment-service-db:5432/appointment_service_db
- HOSPITAL_APP_APPOINTMENT_SERVICE_DB_USER=user
- HOSPITAL_APP_APPOINTMENT_SERVICE_DB_PASS=pass
- HOSPITAL_APP_APPOINTMENT_SERVICE_DB_DRIVER=org.postgresql.Driver
- HOSPITAL_APP_APPOINTMENT_SERVICE_DB_DDL_AUTO=update
- HOSPITAL_APP_APPOINTMENT_SERVICE_DB_DIALECT=org.hibernate.dialect.PostgreSQLDialect

RabbitMQ with TLS
- HOSPITAL_APP_RABBITMQ_HOST=rabbitmq
- HOSPITAL_APP_RABBITMQ_PORT=5671
- HOSPITAL_APP_RABBITMQ_USER=guest
- HOSPITAL_APP_RABBITMQ_PASS=guest
- HOSPITAL_APP_RABBITMQ_SSL_ENABLED=true
- HOSPITAL_APP_RABBITMQ_SSL_ALGO=TLSv1.2
- HOSPITAL_APP_RABBITMQ_SSL_VALIDATE=true
- HOSPITAL_APP_RABBITMQ_KEYSTORE=classpath:rabbitmq/client_keystore.p12
- HOSPITAL_APP_RABBITMQ_KEYSTORE_PASS=changeit
- HOSPITAL_APP_RABBITMQ_TRUSTSTORE=classpath:rabbitmq/truststore.p12
- HOSPITAL_APP_RABBITMQ_TRUSTSTORE_PASS=changeit

User Service (REST fallback and general base URL)
- HOSPITAL_APP_USER_SERVICE_URL=http://user-service:8080

mTLS certs for gRPC client
- HOSPITAL_APP_APPOINTMENT_SERVICE_CRT=classpath:tls/appointment_service.crt
- HOSPITAL_APP_APPOINTMENT_SERVICE_KEY=classpath:tls/appointment_service_pkcs8.key
- HOSPITAL_APP_APPOINTMENT_SERVICE_CA=classpath:tls/ca.crt

Other
- ENABLE_REMOTE_DEBUG=true (when running in Docker, exposes port 5006)


## Security
- Resource server configured to require JWT for most endpoints
- Role mappings enforced by AppointmentSecurityConfiguration
- Swagger endpoints are publicly accessible

To test with JWTs locally, use the jwt_security_common module utilities or any valid token minted by your chosen identity provider that matches the expected signing keys and roles.


## Useful commands
- Build monorepo: mvn -q -DskipTests package
- Run appointment service only: mvn -q -pl appointment_service -am spring-boot:run
- Start only appointment-service and dependencies with Docker:
  docker compose up -d appointment-service appointment-service-db rabbitmq user-service user-service-db
- View logs: docker logs -f hospital-app-appointment-service


## Troubleshooting
- RabbitMQ TLS errors: ensure keystore/truststore paths match the packaged resources and passwords in .env
- gRPC mTLS failures: verify certificates under appointment_service/src/main/resources/tls and that User Service is exposing 9090 with TLS and matching CA
- DB connection failures: confirm appointment-service-db container is healthy and env vars are correct
- 401/403 responses: verify JWT token, roles, and that Authorization: Bearer <token> header is present


## Project paths and references
- Dockerfile: appointment_service/Dockerfile
- Spring config: appointment_service/src/main/resources/application.yml
- TLS files: appointment_service/src/main/resources/tls/
- RabbitMQ config class: infra/config/message/rabbitmq/RabbitMQConfig.java
- Producer: infra/adapter/out/message/AppointmentProducer.java
- REST controller: infra/adapter/in/controller/AppointmentRestController.java
- Security: infra/config/security/AppointmentSecurityConfiguration.java
- gRPC client: infra/adapter/out/user_service/grpc/UserServiceClientGrpcAdapter.java


## License
This project is part of an educational/portfolio repository. See root-level LICENSE if available.
