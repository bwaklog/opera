package com.ooad.opaero.service;
import org.springframework.stereotype.Service;
import com.ooad.opaero.model.Flight;
import com.ooad.opaero.model.Gate;
import com.ooad.opaero.repository.FlightRepository;
import com.ooad.opaero.repository.GateRepository;
import com.ooad.opaero.patterns.strategy.GateAllocationStrategy;
import com.ooad.opaero.patterns.strategy.IntelligentGateStrategy;
import com.ooad.opaero.patterns.observer.FlightNotifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GroundOperationsService {
    private final FlightRepository flightRepo;
    private final GateRepository gateRepo;
    private final FlightNotifier notifier;
    private GateAllocationStrategy allocationStrategy;
    
    public GroundOperationsService(FlightRepository fr, GateRepository gr, FlightNotifier notifier) {
        this.flightRepo = fr;
        this.gateRepo = gr;
        this.notifier = notifier;
        this.allocationStrategy = new IntelligentGateStrategy(); // Default Strategy
    }
    
    public void setAllocationStrategy(GateAllocationStrategy strat) {
        this.allocationStrategy = strat;
    }
    
    public Flight assignGate(Long flightId) {
        Flight f = flightRepo.findById(flightId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found: " + flightId));
        if (f.getGateNumber() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight already has a gate");
        }
        if (!"SCHEDULED".equals(f.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gate can only be assigned while scheduled");
        }
        List<Gate> available = gateRepo.findByOccupiedFalse();
        Gate gate = allocationStrategy.allocateGate(available);
        if (gate == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No available gate");
        }
        f.setGateNumber(gate.getGateId());
        f.setStatus("BOARDING");
        gate.setOccupied(true);
        gateRepo.save(gate);
        Flight updated = flightRepo.save(f);
        notifier.notifyFlightUpdate(updated);
        return updated;
    }

    public List<Gate> listAvailableGates() {
        return gateRepo.findByOccupiedFalse();
    }

    public List<Gate> listGates() {
        return gateRepo.findAll();
    }
}