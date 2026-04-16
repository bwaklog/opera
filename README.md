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

## Project Summary: 

Opaero is a comprehensive Airport and Flight Operations Management System built with Spring Boot and Java. It is an Object-Oriented Analysis & Design (OOAD) project that implements modern design patterns and software engineering principles.

---

## Core Purpose

The system manages airport operations including flights, passengers, gates, and runways through a RESTful API backend. It supports workflows such as flight scheduling, gate allocation, runway management, and passenger check-in.

---

## Technology Stack

- **Backend:** Spring Boot 4.0.5  
- **Language:** Java 17  
- **Database:** H2 (in-memory database)  
- **Architecture Pattern:** Model-View-Controller (MVC)  
- **Build Tools:** Maven & Gradle  
- **API Testing:** Python (`test_opaero_api.py`)  

---

## Key Components

| Component     | Purpose |
|--------------|--------|
| Models       | Flight, Gate, Runway, User entities persisted via JPA |
| Controllers  | Handle HTTP requests: PassengerController, AirlineStaffController, GroundOpsController, AirTrafficController, AdminController |
| Services     | Business logic: FlightService, GroundOperationsService, AirTrafficControlService |
| Repositories | Data persistence layer for all entities |

---

## Design Patterns Implemented

- **Singleton:** `AirportSystemManager` – Global airport configuration  
- **Factory Method:** `UserFactory` – User instantiation by role  
- **Facade:** `PassengerOperationsFacade` – Simplified passenger workflows  
- **Strategy:** `GateAllocationStrategy`, `RunwayAllocationStrategy` – Pluggable allocation algorithms  
- **Observer:** `FlightNotifier`, `FlightObserver` – Flight update broadcasting  
- **State:** `FlightState`, `ScheduledState`, `BoardingState` – Flight lifecycle management  

---

## Core Principles

- Single Responsibility Principle (SRP)  
- Open-Closed Principle (OCP)  
- Dependency Inversion Principle (DIP)  
- Liskov Substitution Principle (LSP)  

---

## Summary

This project is an academic OOAD implementation that demonstrates enterprise-level architecture, clean design principles, and real-world system modeling in the context of airport and flight operations.
