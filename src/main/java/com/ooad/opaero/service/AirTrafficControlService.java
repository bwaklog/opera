package com.ooad.opaero.service;
import com.ooad.opaero.model.Flight;
import com.ooad.opaero.model.Runway;
import com.ooad.opaero.patterns.observer.FlightNotifier;
import com.ooad.opaero.patterns.strategy.FirstAvailableRunwayStrategy;
import com.ooad.opaero.patterns.strategy.RunwayAllocationStrategy;
import com.ooad.opaero.repository.FlightRepository;
import com.ooad.opaero.repository.RunwayRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class AirTrafficControlService {
    private final FlightRepository flightRepo;
    private final RunwayRepository runwayRepo;
    private final FlightNotifier notifier;
    private RunwayAllocationStrategy allocationStrategy;

    public AirTrafficControlService(FlightRepository flightRepo, RunwayRepository runwayRepo, FlightNotifier notifier) {
        this.flightRepo = flightRepo;
        this.runwayRepo = runwayRepo;
        this.notifier = notifier;
        this.allocationStrategy = new FirstAvailableRunwayStrategy();
    }

    public void setAllocationStrategy(RunwayAllocationStrategy strategy) {
        this.allocationStrategy = strategy;
    }

    public Flight assignRunway(Long flightId) {
        Flight flight = flightRepo.findById(flightId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found: " + flightId));
        if (flight.getRunwayId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight already has a runway");
        }
        if (flight.getGateNumber() == null || flight.getGateNumber().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gate required before runway assignment");
        }
        if (!"BOARDING".equals(flight.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Runway can only be assigned while boarding");
        }
        List<Runway> available = runwayRepo.findByOccupiedFalse();
        Runway runway = allocationStrategy.allocateRunway(available);
        if (runway == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No available runway");
        }
        flight.setRunwayId(runway.getRunwayId());
        runway.setOccupied(true);
        runwayRepo.save(runway);
        Flight updated = flightRepo.save(flight);
        notifier.notifyFlightUpdate(updated);
        return updated;
    }

    public Flight issueClearance(Long flightId) {
        Flight flight = flightRepo.findById(flightId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found: " + flightId));
        if (!"TAXIING".equals(flight.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight must be TAXIING for clearance");
        }
        if (flight.getRunwayId() == null || flight.getRunwayId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Runway required before clearance");
        }
        flight.setStatus("CLEARED_FOR_TAKEOFF");
        Flight updated = flightRepo.save(flight);
        notifier.notifyFlightUpdate(updated);
        return updated;
    }

    public List<Runway> listAvailableRunways() {
        return runwayRepo.findByOccupiedFalse();
    }

    public List<Runway> listRunways() {
        return runwayRepo.findAll();
    }
}
