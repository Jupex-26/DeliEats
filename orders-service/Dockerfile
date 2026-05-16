# --- STAGE 1: BUILD THE APPLICATION ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# --- STAGE 2: RUN THE APPLICATION ---
FROM eclipse-temurin:21-jre-alpine

ARG JAR_FILE=target/orders-service-0.0.1-SNAPSHOT.jar

COPY --from=build /app/${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
