package com.ooad.opaero.service;
import com.ooad.opaero.model.Flight;
import com.ooad.opaero.repository.FlightRepository;
import com.ooad.opaero.repository.GateRepository;
import com.ooad.opaero.repository.RunwayRepository;
import com.ooad.opaero.patterns.state.FlightStateContext;
import com.ooad.opaero.patterns.observer.FlightNotifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class FlightService {
    private final FlightRepository flightRepo;
    private final GateRepository gateRepo;
    private final RunwayRepository runwayRepo;
    private final FlightNotifier notifier;
    
    public FlightService(FlightRepository repo, GateRepository gateRepo, RunwayRepository runwayRepo, FlightNotifier notifier) {
        this.flightRepo = repo;
        this.gateRepo = gateRepo;
        this.runwayRepo = runwayRepo;
        this.notifier = notifier;
    }
    
    public Flight scheduleFlight(Flight f) {
        Flight saved = flightRepo.save(f);
        notifier.notifyFlightUpdate(saved);
        return saved;
    }

    public List<Flight> listFlights() {
        return flightRepo.findAll();
    }

    public Flight getFlight(Long flightId) {
        return flightRepo.findById(flightId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found: " + flightId));
    }

    public Flight updateStatus(Long flightId, String status) {
        Flight f = flightRepo.findById(flightId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found: " + flightId));
        String before = f.getStatus();
        validateStatusChange(f, status);
        f.setStatus(status);
        handleResourceTransitions(before, f);
        Flight updated = flightRepo.save(f);
        notifier.notifyFlightUpdate(updated);
        return updated;
    }
    
    public Flight advanceFlightState(Long flightId) {
        Flight f = flightRepo.findById(flightId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found: " + flightId));
        String before = f.getStatus();
        FlightStateContext ctx = new FlightStateContext(f);
        try {
            ctx.nextState(f);
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        handleResourceTransitions(before, f);
        Flight updated = flightRepo.save(f);
        notifier.notifyFlightUpdate(updated);
        return updated;
    }

    private void validateStatusChange(Flight flight, String status) {
        if ("BOARDING".equals(status) && (flight.getGateNumber() == null || flight.getGateNumber().isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gate required before boarding");
        }
        if (("TAXIING".equals(status) || "CLEARED_FOR_TAKEOFF".equals(status))
            && (flight.getRunwayId() == null || flight.getRunwayId().isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Runway required before taxiing/clearance");
        }
    }

    private void handleResourceTransitions(String before, Flight flight) {
        String after = flight.getStatus();
        if ("BOARDING".equals(before) && "TAXIING".equals(after)) {
            if (flight.getGateNumber() != null) {
                gateRepo.findById(flight.getGateNumber()).ifPresent(gate -> {
                    gate.setOccupied(false);
                    gateRepo.save(gate);
                });
                flight.setGateNumber(null);
            }
        }
        if ("CLEARED_FOR_TAKEOFF".equals(before) && "AIRBORNE".equals(after)) {
            if (flight.getRunwayId() != null) {
                runwayRepo.findById(flight.getRunwayId()).ifPresent(runway -> {
                    runway.setOccupied(false);
                    runwayRepo.save(runway);
                });
                flight.setRunwayId(null);
            }
        }
    }
}