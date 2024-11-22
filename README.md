# User Service

## Overview
A web application that manages user authentication and profile operations. Users can register, log in, update their profile, and more.

## Features
- **User Authentication**: Register, log in, log out, update profile and delete account.
- **JWT Authentication**: Token-based authentication using JWT.
- **Profile Management**: Update user profile details and password.

## Prerequisites
- **Java** (JDK 8 or higher)
- **Maven** (v3.6.0 or higher)
- **MySQL** (or any other preferred relational database)
- **Docker** (if using Docker for containerization)
-  **Git**

## Installation

### 1. Clone the Repository
```bash
git clone <repo-url>
cd user-service
```

### 2. Configure Database
Set up your MySQL database and note the connection details.

### 3. Set Up Environment Variables
Create an application.properties file in the src/main/resources directory and add the following properties:
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/<your_database>
spring.datasource.url=jdbc:mysql://host.docker.internal:3306/<your_database> # for docker run
spring.datasource.username=<username>
spring.datasource.password=<your_password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect

# service to service communication URL
orderservice.url=http://localhost:9005/api/orders/user/
```

### 4. Build and Run the Server
Build the project using Maven and run it.
```bash
mvn clean install
mvn spring-boot:run
```

## Docker Setup
### 1. Create a Docker Network
Create a Docker network for communication between containers.
```bash
docker network create <network_name>
```

### 2. Dockerfile
Ensure you have a Dockerfile in the root directory of your project:
```dockerfile
# Use an official JDK runtime as a parent image
FROM openjdk:11-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven build artifact (JAR file) into the container
COPY target/user-service-1.0-SNAPSHOT.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 3. Build Docker Images
Build the Docker image for your user service.
```bash
docker build -t user-service:latest .
```

### 4. Run Docker Containers
Run the order-service container and user-service container on the created Docker network.
```bash
# Run order-service container
docker run --name order-service --network my-network -p 9005:9005 order-service:latest

# Run user-service container
docker run --name user-service --network my-network -p 8080:8080 user-service:latest
```

## API Endpoints
### User Routes
- **POST /api/users**: Register a new user
```json
{
    "name": "your_name",
    "password": "your_password",
    "email": "your_email@example.com",
    "phoneNumber": "123-456-7890",
    "address": "123 Main St",
    "city": "your_city",
    "state": "your_state",
    "country": "your_country",
    "postalCode": "62704",
    "gender": "Male"
}
```

- **POST /api/authenticate**: Log in a user
```json
{
    "username": "example@example.com",
    "password": "your_password"
}
```

- **POST /api/logout**: Log out a user
```http
Authorization: Bearer <JWT_TOKEN>
```

- **GET /api/users/{{user_id}}**: Get user profile
```http
Authorization: Bearer <JWT_TOKEN>
```

- **PUT /api/users/{{user_id}}**: Update user profile
```http
Authorization: Bearer <JWT_TOKEN>
```
```json
{
    "name": "your_name",
    "password": "your_password",
    "email": "your_email@example.com",
    "phoneNumber": "123-456-7890",
    "address": "123 Main St",
    "city": "your_city",
    "state": "your_state",
    "country": "your_country",
    "postalCode": "62704",
    "gender": "Male"
}
```

- **DELETE /api/users/{{user_id}}**: Delete a user
```http
Authorization: Bearer <JWT_TOKEN>
```

- **GET /api/users/authstatus**: Get Authentication Status
```json
{
    "token": "<token_to_check_auth_status>",
    "userId": <id_of_user_to_get_auth_status>
}

```

## Usage
- Register a new user.

- Log in using your credentials to receive a JWT token.

- Use the JWT token to access protected endpoints.

- Update and delete user profile.

- Check authentication status

## License
[MIT](https://choosealicense.com/licenses/mit/?form=MG0AV3)