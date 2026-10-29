# Build and test with the project's Gradle wrapper and an official JDK.
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew
COPY src ./src
# Tests use an isolated H2 database before the JAR is packaged.
RUN ./gradlew test bootJar --no-daemon --console=plain

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S student && adduser -S student -G student
COPY --from=builder --chown=student:student /workspace/build/libs/student-api.jar app.jar
USER student
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
