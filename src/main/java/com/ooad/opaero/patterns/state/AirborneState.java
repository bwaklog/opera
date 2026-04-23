package com.ooad.opaero.patterns.state;
import com.ooad.opaero.model.Flight;

public class AirborneState implements FlightState {
    @Override
    public void next(Flight flight, FlightStateContext context) {
        flight.setStatus("AIRBORNE");
    }

    @Override
    public void prev(Flight flight, FlightStateContext context) {
        flight.setStatus("CLEARED_FOR_TAKEOFF");
        context.setState(new ClearedForTakeoffState());
    }

    @Override
    public void printStatus() { System.out.println("Status is AIRBORNE"); }
}
