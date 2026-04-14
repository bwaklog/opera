package com.ooad.opaero.controller;
import com.ooad.opaero.patterns.facade.PassengerOperationsFacade;
import com.ooad.opaero.model.Flight;
import com.ooad.opaero.repository.FlightRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/passenger")
public class PassengerController {
    
    private final PassengerOperationsFacade passengerFacade;
    private final FlightRepository flightRepo;
    
    public PassengerController(PassengerOperationsFacade p, FlightRepository f) {
        this.passengerFacade = p;
        this.flightRepo = f;
    }
    
    @GetMapping("/flights/search")
    public List<Flight> searchFlights(@RequestParam String dest) {
        return flightRepo.findByDestination(dest);
    }
    
    @PostMapping("/checkin")
    public boolean checkin(@RequestParam Long passengerId, @RequestParam Long flightId) {
        return passengerFacade.performCheckIn(passengerId, flightId);
    }
}