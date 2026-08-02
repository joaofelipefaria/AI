# Use OpenJDK image as base
FROM eclipse-temurin:17

# Set working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY ../target/api-0.0.1-SNAPSHOT.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]