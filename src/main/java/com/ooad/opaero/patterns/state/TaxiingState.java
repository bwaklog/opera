package com.ooad.opaero.patterns.state;
import com.ooad.opaero.model.Flight;

public class TaxiingState implements FlightState {
    @Override
    public void next(Flight flight, FlightStateContext context) {
        if (flight.getRunwayId() == null || flight.getRunwayId().isBlank()) {
            throw new IllegalStateException("Runway required before clearance");
        }
        flight.setStatus("CLEARED_FOR_TAKEOFF");
        context.setState(new ClearedForTakeoffState());
    }

    @Override
    public void prev(Flight flight, FlightStateContext context) {
        flight.setStatus("BOARDING");
        context.setState(new BoardingState());
    }

    @Override
    public void printStatus() { System.out.println("Status is TAXIING"); }
}
