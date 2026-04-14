package com.ooad.opaero.controller;
import com.ooad.opaero.model.Flight;
import com.ooad.opaero.model.Gate;
import com.ooad.opaero.service.GroundOperationsService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/groundops")
public class GroundOpsController {
    
    private final GroundOperationsService groundOpsService;
    
    public GroundOpsController(GroundOperationsService g) {
        this.groundOpsService = g;
    }
    
    @PostMapping("/assign-gate/{flightId}")
    public Flight assignGate(@PathVariable Long flightId) {
        return groundOpsService.assignGate(flightId);
    }

    @GetMapping("/gates/available")
    public List<Gate> listAvailableGates() {
        return groundOpsService.listAvailableGates();
    }
}