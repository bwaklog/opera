# Opaero

## Build
```bash
./mvnw clean package
```

## Test
```bash
./mvnw test
- python test_opaero_api.py
```

## About the project

Opaero is a Spring Boot–based airport and flight operations management system designed using the Model-View-Controller (MVC) architecture. It simulates real-world airport workflows including flight scheduling, passenger check-in, gate allocation, runway assignment, and air traffic control operations.

The system is built with a strong focus on clean architecture and object-oriented design principles such as SRP, OCP, and DIP. It incorporates multiple design patterns including Strategy (for gate/runway allocation), State (for flight lifecycle management), Observer (for flight notifications), Factory (for user creation), and Facade (for simplified passenger operations). :contentReference[oaicite:0]{index=0}

Key features include:
- RESTful APIs for different roles: passenger, airline staff, ground operations, ATC, and admin
- Flight lifecycle management (Scheduled → Boarding → Taxiing → Takeoff)
- Dynamic gate and runway allocation strategies
- In-memory database support using H2 for rapid testing
- API documentation via OpenAPI
- End-to-end integration testing using Python-based test suites :contentReference[oaicite:1]{index=1}

The project emphasizes modularity, scalability, and real-world simulation of airport operations while maintaining clean separation of concerns and extensibility.
