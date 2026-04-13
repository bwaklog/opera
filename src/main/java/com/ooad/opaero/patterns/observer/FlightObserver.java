package com.ooad.opaero.patterns.observer;
import com.ooad.opaero.model.Flight;

public interface FlightObserver {
    void update(Flight flight);
}