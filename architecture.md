# Opaero - Airport and Flight Operations Management Architecture

## Architecture Pattern
**Model-View-Controller (MVC)**
Spring Boot provides the MVC structure:
- **Model**: Domain entities (`Flight`, `Gate`, `Runway`, `User`) persisted via JPA.
- **View**: REST endpoints (no GUI) expose JSON as the view layer.
- **Controller**: HTTP orchestration and routing (`PassengerController`, `AirlineStaffController`, `GroundOpsController`, `AirTrafficController`, `AdminController`).

Example controller delegating to service (controller layer):
```java
@RestController
@RequestMapping("/api/airline")
public class AirlineStaffController {
    private final FlightService flightService;

    @PostMapping("/schedule")
    public Flight scheduleFlight(@RequestBody Flight flight) {
        return flightService.scheduleFlight(flight);
    }
}
```

Example model entity (model layer):
```java
@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String flightNumber;
    private String status = "SCHEDULED";
}
```

## Design Principles
1. **Single Responsibility Principle (SRP)**
   Controllers only handle HTTP, services only handle business logic, and repositories only handle persistence.
   ```java
   @Service
   public class FlightService {
       public Flight scheduleFlight(Flight f) {
           return flightRepo.save(f);
       }
   }
   ```
2. **Open-Closed Principle (OCP)**
   Gate/runway algorithms can be swapped without changing services.
   ```java
   public interface GateAllocationStrategy {
       Gate allocateGate(List<Gate> availableGates);
   }
   ```
3. **Dependency Inversion Principle (DIP)**
   Services depend on interfaces/abstractions (`FlightRepository`, `GateAllocationStrategy`).
   ```java
   public GroundOperationsService(FlightRepository fr, GateRepository gr) {
       this.flightRepo = fr;
       this.gateRepo = gr;
   }
   ```
4. **Liskov Substitution Principle (LSP)**
   Different states implement the same `FlightState` and are interchangeable in context.
   ```java
   public interface FlightState {
       void next(Flight flight, FlightStateContext context);
   }
   ```

## Design Patterns

### Creational Patterns
1. **Singleton Pattern**
   - **Component**: `AirportSystemManager`
   - **Usage**: Global airport configuration managed as a Spring singleton.
   ```java
   @Component
   public class AirportSystemManager {
       private String currentIataCode = "BLR";
   }
   ```
2. **Factory Method Pattern**
   - **Component**: `UserFactory`
   - **Usage**: Centralizes user instantiation by role.
   ```java
   public User createUser(String role, String username) {
       User user = new User();
       user.setRole(role.toUpperCase());
       return user;
   }
   ```

### Structural Patterns
3. **Facade Pattern**
   - **Component**: `PassengerOperationsFacade`
   - **Usage**: Simplifies passenger workflows into a single call.
   ```java
   public boolean performCheckIn(Long passengerId, Long flightId) {
       Flight f = flightRepository.findById(flightId).orElse(null);
       return f != null && "SCHEDULED".equals(f.getStatus());
   }
   ```

### Behavioral Patterns
4. **Strategy Pattern**
   - **Components**: `GateAllocationStrategy`, `RunwayAllocationStrategy`
   - **Usage**: Allows swapping allocation logic for gates/runways.
   ```java
   public class FirstAvailableRunwayStrategy implements RunwayAllocationStrategy {
       public Runway allocateRunway(List<Runway> availableRunways) {
           return availableRunways.isEmpty() ? null : availableRunways.get(0);
       }
   }
   ```
5. **Observer Pattern**
   - **Components**: `FlightNotifier`, `FlightObserver`
   - **Usage**: Broadcasts updates to subscribers.
   ```java
   public void notifyFlightUpdate(Flight flight) {
       for (FlightObserver obs : observers) {
           obs.update(flight);
       }
   }
   ```
6. **State Pattern**
   - **Components**: `FlightState`, `ScheduledState`, `BoardingState`
   - **Usage**: Controls valid transitions in flight lifecycle.
   ```java
   public class ScheduledState implements FlightState {
       public void next(Flight flight, FlightStateContext context) {
           flight.setStatus("BOARDING");
           context.setState(new BoardingState());
       }
   }
   ```