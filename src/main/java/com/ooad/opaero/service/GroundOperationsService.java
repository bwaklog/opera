package com.ooad.opaero.service;
import org.springframework.stereotype.Service;
import com.ooad.opaero.model.Flight;
import com.ooad.opaero.model.Gate;
import com.ooad.opaero.repository.FlightRepository;
import com.ooad.opaero.repository.GateRepository;
import com.ooad.opaero.patterns.strategy.GateAllocationStrategy;
import com.ooad.opaero.patterns.strategy.IntelligentGateStrategy;

import java.util.List;

@Service
public class GroundOperationsService {
    private final FlightRepository flightRepo;
    private final GateRepository gateRepo;
    private GateAllocationStrategy allocationStrategy;
    
    public GroundOperationsService(FlightRepository fr, GateRepository gr) {
        this.flightRepo = fr;
        this.gateRepo = gr;
        this.allocationStrategy = new IntelligentGateStrategy(); // Default Strategy
    }
    
    public void setAllocationStrategy(GateAllocationStrategy strat) {
        this.allocationStrategy = strat;
    }
    
    public Flight assignGate(Long flightId) {
        Flight f = flightRepo.findById(flightId).orElseThrow();
        List<Gate> available = gateRepo.findByOccupiedFalse();
        Gate gate = allocationStrategy.allocateGate(available);
        if(gate != null) {
            f.setGateNumber(gate.getGateId());
            gate.setOccupied(true);
            gateRepo.save(gate);
        }
        return flightRepo.save(f); // Saves cascade behavior
    }

    public List<Gate> listAvailableGates() {
        return gateRepo.findByOccupiedFalse();
    }
}