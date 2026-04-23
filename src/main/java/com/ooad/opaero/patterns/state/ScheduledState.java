package com.ooad.opaero.patterns.state;
import com.ooad.opaero.model.Flight;

public class ScheduledState implements FlightState {
    @Override
    public void next(Flight flight, FlightStateContext context) {
        if (flight.getGateNumber() == null || flight.getGateNumber().isBlank()) {
            throw new IllegalStateException("Gate required before boarding");
        }
        flight.setStatus("BOARDING");
        context.setState(new BoardingState());
    }
    @Override
    public void prev(Flight flight, FlightStateContext context) {
        System.out.println("Flight is in its root state");
    }
    @Override
    public void printStatus() { System.out.println("Status is SCHEDULED"); }
}