package com.ooad.opaero.patterns.state;
import com.ooad.opaero.model.Flight;

public class FlightStateContext {
    private FlightState state = new ScheduledState();
    
    public void setState(FlightState state) { this.state = state; }
    
    public void nextState(Flight flight) {
        state.next(flight, this);
    }
}