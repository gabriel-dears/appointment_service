# =========================
# Stage 1: Build
# =========================
FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app

# Copy jwt_security_common first
COPY jwt_security_common /app/jwt_security_common
RUN mvn -f /app/jwt_security_common/pom.xml clean install -DskipTests

# Copy appointment_service pom and fetch dependencies
COPY appointment_service/pom.xml /app/appointment_service/pom.xml
RUN mvn -f /app/appointment_service/pom.xml dependency:go-offline -B

# Copy appointment_service source and build
COPY appointment_service/src /app/appointment_service/src
RUN mvn -f /app/appointment_service/pom.xml clean package -DskipTests

# =========================
# Stage 2: Runtime
# =========================
FROM openjdk:21-jdk-slim

RUN groupadd --gid 1000 appgroup \
    && useradd --uid 1000 --gid appgroup --shell /bin/sh --create-home appuser

WORKDIR /app

COPY --from=build /app/appointment_service/target/appointment_service-0.0.1-SNAPSHOT.jar app.jar
RUN chown appuser:appgroup /app/app.jar
USER appuser

EXPOSE 8080 5005

ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "if [ \"$ENABLE_REMOTE_DEBUG\" = 'true' ]; then java $JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5005 -jar app.jar; else java $JAVA_OPTS -jar app.jar; fi"]
