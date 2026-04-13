package com.ooad.opaero.patterns.singleton;

import org.springframework.stereotype.Component;

// Singleton pattern using Spring Context
@Component
public class AirportSystemManager {
    private String currentIataCode = "BLR";

    public String getIataCode() {
        return currentIataCode;
    }

    public void setIataCode(String iataCode) {
        this.currentIataCode = iataCode;
    }
}