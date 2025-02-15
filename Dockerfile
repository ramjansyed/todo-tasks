# Use an official Gradle image as the base image
FROM gradle:8.10.0-jdk17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the build.gradle and settings.gradle files to the container
COPY build.gradle settings.gradle ./

# Copy the rest of the project files into the container
COPY src ./src

# Build the application using Gradle
RUN gradle shadowJar --no-daemon

# Use a minimal JRE image for the final container
FROM openjdk:17-jdk-slim

# Set the working directory for the application
WORKDIR /app

# Copy the compiled JAR file from the build stage
COPY --from=build /app/build/libs/*.jar /app/app.jar

# Copy the config.yml file from resource folder
COPY src/main/resources/config.yml /app/config.yml

# Expose the application port
EXPOSE 8080

# Set the environment variable for Dropwizard config
ENV DROPWIZARD_CONFIG=/app/config.yml

# Run the application when the container starts
ENTRYPOINT ["java", "-jar", "/app/app.jar", "server", "/app/config.yml"]
