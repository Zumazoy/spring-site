FROM maven:3-openjdk-23 AS builder

RUN app-get update && \
    app-get install -y maven

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:23-slim

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 2025

CMD ["java", "-jar", "app.jar"]