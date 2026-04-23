package com.ooad.opaero.patterns.state;
import com.ooad.opaero.model.Flight;

public class BoardingState implements FlightState {
    @Override
    public void next(Flight flight, FlightStateContext context) {
        if (flight.getRunwayId() == null || flight.getRunwayId().isBlank()) {
            throw new IllegalStateException("Runway required before taxiing");
        }
        flight.setStatus("TAXIING");
        context.setState(new TaxiingState());
    }
    @Override
    public void prev(Flight flight, FlightStateContext context) {
        flight.setStatus("SCHEDULED");
        context.setState(new ScheduledState());
    }
    @Override
    public void printStatus() { System.out.println("Status is BOARDING"); }
}