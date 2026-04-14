package com.ooad.opaero.controller;
import com.ooad.opaero.model.Flight;
import com.ooad.opaero.service.FlightService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/airline")
public class AirlineStaffController {
    
    private final FlightService flightService;
    
    public AirlineStaffController(FlightService flightService) {
        this.flightService = flightService;
    }
    
    @PostMapping("/schedule")
    public Flight scheduleFlight(@RequestBody Flight flight) {
        return flightService.scheduleFlight(flight);
    }

    @GetMapping("/flights")
    public List<Flight> listFlights() {
        return flightService.listFlights();
    }

    @GetMapping("/flights/{id}")
    public Flight getFlight(@PathVariable Long id) {
        return flightService.getFlight(id);
    }

    @PatchMapping("/flights/{id}/status")
    public Flight updateStatus(@PathVariable Long id, @RequestParam String status) {
        return flightService.updateStatus(id, status);
    }
    
    @PostMapping("/{id}/advance")
    public Flight advanceFlightStatus(@PathVariable Long id) {
        return flightService.advanceFlightState(id);
    }
}