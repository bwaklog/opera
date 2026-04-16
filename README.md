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

## About

Opaero is a Spring Boot-based airport and flight operations management system that simulates real-world airport workflows. It handles flight scheduling, passenger check-in, gate allocation, runway assignment, and air traffic control operations—basically everything that happens behind the scenes at an airport.

The project is built with clean architecture and solid object-oriented design principles. It uses multiple design patterns like Strategy (for smarter gate/runway allocation), State (to manage flight lifecycles), Observer (to notify about flight updates), Factory (for creating users), and Facade (to simplify passenger operations).

---

## What it does

- REST APIs for passengers, airline staff, ground operations, ATC, and admins  
- Flight lifecycle management (Scheduled → Boarding → Taxiing → Takeoff)  
- Dynamic gate and runway allocation strategies  
- H2 in-memory database for quick testing  
- OpenAPI documentation  
- Python-based integration tests  

---
