package com.ooad.opaero.service;
import com.ooad.opaero.model.Flight;
import com.ooad.opaero.model.Runway;
import com.ooad.opaero.patterns.observer.FlightNotifier;
import com.ooad.opaero.patterns.strategy.FirstAvailableRunwayStrategy;
import com.ooad.opaero.patterns.strategy.RunwayAllocationStrategy;
import com.ooad.opaero.repository.FlightRepository;
import com.ooad.opaero.repository.RunwayRepository;
import org.springframework.stereotype.Service;
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
        Flight flight = flightRepo.findById(flightId).orElseThrow();
        List<Runway> available = runwayRepo.findByOccupiedFalse();
        Runway runway = allocationStrategy.allocateRunway(available);
        if (runway != null) {
            flight.setRunwayId(runway.getRunwayId());
            runway.setOccupied(true);
            runwayRepo.save(runway);
            flight.setStatus("TAXIING");
        }
        Flight updated = flightRepo.save(flight);
        notifier.notifyFlightUpdate(updated);
        return updated;
    }

    public Flight issueClearance(Long flightId) {
        Flight flight = flightRepo.findById(flightId).orElseThrow();
        flight.setStatus("CLEARED_FOR_TAKEOFF");
        Flight updated = flightRepo.save(flight);
        notifier.notifyFlightUpdate(updated);
        return updated;
    }

    public List<Runway> listAvailableRunways() {
        return runwayRepo.findByOccupiedFalse();
    }
}
