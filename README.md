# IT Support Ticket System

## 📌 Overview
This project is a simple **IT Support Ticket System** that allows employees to report and track IT issues. The system includes user authentication, ticket management, audit logs, and role-based access.

## 🚀 Features
- **User Authentication**: Employees & IT Support roles with login functionality.
- **Ticket Management**: Employees can create tickets with title, description, priority, and category.
- **Status Tracking**: IT Support can update ticket statuses (NEW, IN_PROGRESS, RESOLVED).
- **Audit Logs**: Tracks changes to ticket statuses and added comments.
- **Search & Filter**: Search by ticket ID and filter tickets by status.
- **Database Support**: Uses **Oracle Database** for data storage.
- **REST API**: Exposed using **Spring Boot & Swagger Documentation**.
- **Docker Deployment**: Easily deployable with `docker-compose`.

## 📂 Project Structure
```
src/
├── main/
│   ├── java/com/itsupport/ticketsystem/
│   │   ├── config/       # application configuration
│   │   ├── controller/   # REST API Controllers
│   │   ├── model/        # Entity Models
│   │   ├── repository/   # Spring Data JPA Repositories
│   │   ├── service/      # Business Logic Services
│   │   ├── security/     # Authentication & Authorization
│   │   ├── ui/           # Java Swing UI Components
│   │   ├── utils/        # Utility Classes (Rest Client)
│   ├── resources/
│   │   ├── application.properties  # Database & Configs
│   │   ├── openapi.yaml  			# OpenAPI documentation 
├── test/                # Unit & Integration Tests
```

## 🛠️ Technologies Used
- **Backend**: Java 17, Spring Boot, Spring Security, Spring Data JPA
- **Frontend**: Java Swing (for desktop UI)
- **Database**: Oracle DB
- **Testing**: JUnit 5, Mockito
- **Deployment**: Docker, Docker Compose

## 🔧 Setup & Installation

### 1️⃣ Clone the Repository
```sh
git clone https://github.com/Soulimoo/it-support-ticket-system.git
cd it-support-ticket-system
```

### 2️⃣ Build the Project
Ensure **Java 17** and **Maven** are installed:
```sh
mvn clean install
```

### 3️⃣ Run the Application (Local)
Update the `application.properties` file with your Oracle DB credentials and start the Spring Boot application:
```sh
mvn spring-boot:run
```

### 4️⃣ Run with Docker
#### **Build & Start Containers**
```sh
docker-compose up --build
```

#### **Stop Containers**
```sh
docker-compose down
```

### 5️⃣ API Documentation (Swagger UI)
Once the application is running, open [Swagger UI](http://localhost:8080/swagger-ui.html) in your browser to explore the REST API.

## 🧪 Running Tests
Execute unit and integration tests using:
```sh
mvn test
```

## 📖 API Documentation (Swagger)
This project includes **Swagger/OpenAPI** documentation. To generate and view the API documentation:
- Visit: **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**
- The OpenAPI spec is available at **`/v3/api-docs`**.

## 👨‍💻 Contributing
Feel free to fork this repository, create a branch, and submit a pull request. Contributions are welcome!

---
🚀 **Developed by [Soulimoo](https://github.com/Soulimoo/it-support-ticket-system)**
