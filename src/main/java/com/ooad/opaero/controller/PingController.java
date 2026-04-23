package com.ooad.opaero.controller;

import com.ooad.opaero.patterns.singleton.AirportSystemManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class PingController {
    private final AirportSystemManager systemManager;

    public PingController(AirportSystemManager systemManager) {
        this.systemManager = systemManager;
    }

    @GetMapping("/ping")
    public Map<String, String> ping() {
        return Map.of("status", "ok", "iata", systemManager.getIataCode());
    }
}
