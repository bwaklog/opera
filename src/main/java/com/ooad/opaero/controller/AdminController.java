package com.ooad.opaero.controller;
import com.ooad.opaero.model.Flight;
import com.ooad.opaero.model.Gate;
import com.ooad.opaero.model.Runway;
import com.ooad.opaero.repository.FlightRepository;
import com.ooad.opaero.repository.GateRepository;
import com.ooad.opaero.repository.RunwayRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final GateRepository gateRepo;
    private final RunwayRepository runwayRepo;
    private final FlightRepository flightRepo;

    public AdminController(GateRepository gateRepo, RunwayRepository runwayRepo, FlightRepository flightRepo) {
        this.gateRepo = gateRepo;
        this.runwayRepo = runwayRepo;
        this.flightRepo = flightRepo;
    }

    @PostMapping("/gates")
    public Gate createGate(@RequestBody Gate gate) {
        return gateRepo.save(gate);
    }

    @GetMapping("/gates")
    public List<Gate> listGates() {
        return gateRepo.findAll();
    }

    @PostMapping("/runways")
    public Runway createRunway(@RequestBody Runway runway) {
        return runwayRepo.save(runway);
    }

    @GetMapping("/runways")
    public List<Runway> listRunways() {
        return runwayRepo.findAll();
    }

    @GetMapping("/flights")
    public List<Flight> listFlights() {
        return flightRepo.findAll();
    }

    @PostMapping("/seed")
    public void seed() {
        if (gateRepo.count() == 0) {
            Gate g1 = new Gate();
            g1.setGateId("A1");
            g1.setTerminalProximity("T1");
            g1.setOccupied(false);
            gateRepo.save(g1);
            Gate g2 = new Gate();
            g2.setGateId("A2");
            g2.setTerminalProximity("T1");
            g2.setOccupied(false);
            gateRepo.save(g2);
        }
        if (runwayRepo.count() == 0) {
            Runway r1 = new Runway();
            r1.setRunwayId("R1");
            r1.setLength(3800.0);
            r1.setOccupied(false);
            runwayRepo.save(r1);
            Runway r2 = new Runway();
            r2.setRunwayId("R2");
            r2.setLength(4200.0);
            r2.setOccupied(false);
            runwayRepo.save(r2);
        }
    }
}
