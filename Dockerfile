# --- STAGE 1: The Build ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# 1. Copy Maven wrapper and dependencies list
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# 2. Download dependencies (Offline mode)
RUN ./mvnw dependency:go-offline

# 3. Copy source code and build the JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests

# --- STAGE 2: The Runtime ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 4. Copy ONLY the finished JAR from the build stage
COPY --from=build /app/target/sevana.jar app.jar

# 5. Security and Execution
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
