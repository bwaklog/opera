package com.ooad.opaero.controller;
import com.ooad.opaero.model.Flight;
import com.ooad.opaero.model.Runway;
import com.ooad.opaero.service.AirTrafficControlService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/atc")
public class AirTrafficController {
    private final AirTrafficControlService atcService;

    public AirTrafficController(AirTrafficControlService atcService) {
        this.atcService = atcService;
    }

    @PostMapping("/assign-runway/{flightId}")
    public Flight assignRunway(@PathVariable Long flightId) {
        return atcService.assignRunway(flightId);
    }

    @PostMapping("/issue-clearance/{flightId}")
    public Flight issueClearance(@PathVariable Long flightId) {
        return atcService.issueClearance(flightId);
    }

    @GetMapping("/runways/available")
    public List<Runway> listAvailableRunways() {
        return atcService.listAvailableRunways();
    }
}
