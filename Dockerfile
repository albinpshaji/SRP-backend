#STAGE 1: The Build
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

#Copy Maven wrapper and dependencies list
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

#Download dependencies (Offline mode)
RUN ./mvnw dependency:go-offline

#Copy source code and build the JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests

#STAGE 2:Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

#Copy ONLY the finished JAR from the build stage
COPY --from=build /app/target/sevana.jar app.jar

#Security and Execution
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
