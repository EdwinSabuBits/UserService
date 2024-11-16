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
spring.datasource.username=<username>
spring.datasource.password=<your_password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
```

### 4. Build and Run the Server
Build the project using Maven and run it.
```bash
mvn clean install
mvn spring-boot:run
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

## Usage
- Register a new user.

- Log in using your credentials to receive a JWT token.

- Use the JWT token to access protected endpoints.

- Update and delete user profile.

## License
[MIT](https://choosealicense.com/licenses/mit/?form=MG0AV3)