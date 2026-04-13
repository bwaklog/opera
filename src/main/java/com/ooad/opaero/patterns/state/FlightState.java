package com.ooad.opaero.patterns.state;
import com.ooad.opaero.model.Flight;

// State Pattern 
public interface FlightState {
    void next(Flight flight, FlightStateContext context);
    void prev(Flight flight, FlightStateContext context);
    void printStatus();
}