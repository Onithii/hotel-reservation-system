# Hotel Reservation System

A backend-focused hotel reservation application developed using **Java, Spring Boot, and MySQL**. The system provides REST APIs for user registration, room management, room availability checking, and hotel reservations.

## Features

- **User Registration** – Register new users through a REST API.
- **Room Management** – Retrieve and manage hotel room information.
- **Room Availability** – Check room availability through dedicated API endpoints.
- **Reservation Management** – Support hotel reservation operations through REST APIs.
- **Database Integration** – Store and manage application data using MySQL.
- **RESTful API Architecture** – Provide structured endpoints for client-server communication.

## Technologies Used

- **Language:** Java
- **Backend Framework:** Spring Boot 3.3.4
- **JDK:** Java 21
- **Database:** MySQL
- **API Architecture:** REST API
- **Build Tool:** Maven (if configured in the project)

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/users/register` | Register a new user |
| GET | `/rooms` | Retrieve room information |
| GET | `/rooms/available` | Check available rooms |
| `/api/reservations` | Reservation operations; verify the implemented HTTP methods | Manage reservations |

*Note: Confirm the reservation endpoint's exact path and supported HTTP methods in your implementation.*

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 21
- MySQL Server
- Maven, if using the Maven build configuration
- An IDE such as IntelliJ IDEA or Eclipse

### Installation

1. Clone the repository:

   ```bash
   git clone YOUR_REPOSITORY_URL
   ```

2. Open the project in your preferred IDE.

3. Create a MySQL database for the application.

4. Configure the database connection in `application.properties` or `application.yml` using your local database credentials.

5. Run the Spring Boot application using your IDE or the configured Maven wrapper.

6. Test the available REST API endpoints using Postman or another API client.

## Project Structure

```text
hotel-reservation-system/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/hotel_reservation_system/
│       └── resources/
│           └── application.properties
├── pom.xml
└── README.md
```

*The structure above is illustrative. Adapt it to match your actual repository.*

## Future Improvements

- JWT-based authentication and authorization.
- Enhanced reservation validation and management.
- Improved error handling and input validation.
- Additional automated tests.
- Modular service architecture.

## Academic Project

Developed as part of the Higher Diploma in Software Engineering programme at the National Institute of Business Management (NIBM), Sri Lanka.

**Focus Areas:** Backend Development · REST APIs · Database Integration · Software Engineering

---

*This project is intended for educational purposes.*
