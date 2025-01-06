# Stage 1: Build the application
FROM gradle:7.6.1-jdk17 AS build

# Set the working directory
WORKDIR /app

# Copy the Gradle build files
COPY app/build.gradle settings.gradle ./
COPY gradle ./gradle

# Copy the source code
COPY app/src ./src

# Build the application
RUN gradle build --no-daemon

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
