# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/app.jar app.jar

# Expose the port the app runs on
EXPOSE 6868

# Command to run the application
CMD ["java", "-jar", "app.jar"]
