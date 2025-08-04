# Build stage
FROM maven:3.9.11-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean
RUN mvn install -DskipTests
RUN cp target/*.jar app.jar

# Run stage
FROM eclipse-temurin:21
WORKDIR /app
COPY --from=builder /app/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
