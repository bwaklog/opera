package com.ooad.opaero.service;
import com.ooad.opaero.model.Flight;
import com.ooad.opaero.repository.FlightRepository;
import com.ooad.opaero.patterns.state.FlightStateContext;
import com.ooad.opaero.patterns.observer.FlightNotifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class FlightService {
    private final FlightRepository flightRepo;
    private final FlightNotifier notifier;
    
    public FlightService(FlightRepository repo, FlightNotifier notifier) {
        this.flightRepo = repo;
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
        f.setStatus(status);
        Flight updated = flightRepo.save(f);
        notifier.notifyFlightUpdate(updated);
        return updated;
    }
    
    public Flight advanceFlightState(Long flightId) {
        Flight f = flightRepo.findById(flightId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found: " + flightId));
        FlightStateContext ctx = new FlightStateContext();
        ctx.nextState(f);
        Flight updated = flightRepo.save(f);
        notifier.notifyFlightUpdate(updated);
        return updated;
    }
}