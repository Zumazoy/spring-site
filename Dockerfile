FROM maven:3.9.6-openjdk-23 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:23-jdk-slim

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 2025

CMD ["java", "-jar", "app.jar"]