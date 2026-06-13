# ==========================================
# Stage 1: Build the Spring Boot Application
# ==========================================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set working directory inside the container
WORKDIR /app

# Copy the pom.xml first to download dependencies and cache this layer
COPY xenon-backend/pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of the application source code
COPY xenon-backend/src ./src

# Package the application (skip tests for faster builds during deployment)
RUN mvn clean package -DskipTests

# ==========================================
# Stage 2: Minimal Runtime Environment
# ==========================================
FROM eclipse-temurin:17-jre-jammy

# Define metadata
LABEL maintainer="Xeno CRM"
LABEL description="Xeno CRM Backend - Sovereign AI Agent"

# Set the working directory
WORKDIR /app

# Create a non-root user and group for better security
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Copy only the compiled JAR file from the builder stage
# (This drastically reduces the final image size)
COPY --from=builder /app/target/*.jar app.jar

# Expose the standard Spring Boot port
EXPOSE 8080

# Environment variables for JVM optimization in containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENV SPRING_PROFILES_ACTIVE="prod"

# Set the entrypoint to run the JAR file
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
