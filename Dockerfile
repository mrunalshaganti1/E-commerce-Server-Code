FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the host machine to the container
COPY ./target/E-Commerce-Java-Full-Stack--Backend-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port your application will run on
EXPOSE 5454

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
