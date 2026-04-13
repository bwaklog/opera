package com.ooad.opaero.patterns.facade;
import org.springframework.stereotype.Service;
import com.ooad.opaero.repository.FlightRepository;
import com.ooad.opaero.model.Flight;

@Service
public class PassengerOperationsFacade {
    private final FlightRepository flightRepository;
    
    public PassengerOperationsFacade(FlightRepository flightRepo) {
        this.flightRepository = flightRepo;
    }
    
    // Simplifies check-in routing, seat assignment, baggage into one call
    public boolean performCheckIn(Long passengerId, Long flightId) {
        Flight f = flightRepository.findById(flightId).orElse(null);
        if(f != null && "SCHEDULED".equals(f.getStatus())) {
            // Logic for allocating seat, logging baggage.. etc.
            return true;
        }
        return false;
    }
}