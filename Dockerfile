# Use a lightweight OpenJDK image as the base
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
# All subsequent commands will run from this directory
WORKDIR /app

# The 'target/mock-spotify-backend-0.0.1-SNAPSHOT.jar' is the path to your
# compiled application JAR after you run './mvnw clean package'.
# 'app.jar' is the name it will have inside the container.
COPY target/cadenza_backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that your Spring Boot application listens on (default is 8080)
# This tells Docker that the container will use this port.
EXPOSE 8080

# Define the command to run when the container starts
# This executes the Spring Boot application using the copied JAR file.
ENTRYPOINT ["java", "-jar", "app.jar"]
