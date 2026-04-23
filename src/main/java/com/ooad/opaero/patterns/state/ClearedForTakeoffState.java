package com.ooad.opaero.patterns.state;
import com.ooad.opaero.model.Flight;

public class ClearedForTakeoffState implements FlightState {
    @Override
    public void next(Flight flight, FlightStateContext context) {
        flight.setStatus("AIRBORNE");
        context.setState(new AirborneState());
    }

    @Override
    public void prev(Flight flight, FlightStateContext context) {
        flight.setStatus("TAXIING");
        context.setState(new TaxiingState());
    }

    @Override
    public void printStatus() { System.out.println("Status is CLEARED_FOR_TAKEOFF"); }
}
