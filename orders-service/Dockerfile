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

# Copy the uploads folder from the build context to the root of the app in the final image
COPY uploads ./uploads

# Expose the port that your Spring Boot application runs on (default is 8080)
EXPOSE 8080

# Define the entrypoint to run the JAR application
ENTRYPOINT ["java", "-jar", "app.jar"]
