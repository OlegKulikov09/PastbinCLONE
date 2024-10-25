# Use the official image as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY target/PasteProject-0.0.1-SNAPSHOT.jar /app/app.jar

# Run the application
CMD ["java", "-jar", "/app/app.jar"]

EXPOSE 8080