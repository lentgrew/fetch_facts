FROM eclipse-temurin:21-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the packaged application JAR into the container
COPY build/quarkus-app/lib/ /app/lib/
COPY build/quarkus-app/quarkus-run.jar /app/quarkus-run.jar
COPY build/quarkus-app/app/ /app/app/
COPY build/quarkus-app/quarkus/ /app/quarkus/

# Expose the application port
EXPOSE 8080

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "/app/quarkus-run.jar"]