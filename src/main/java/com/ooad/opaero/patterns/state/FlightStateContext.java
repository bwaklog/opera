package com.ooad.opaero.patterns.state;
import com.ooad.opaero.model.Flight;

public class FlightStateContext {
    private FlightState state = new ScheduledState();

    public FlightStateContext() {}

    public FlightStateContext(Flight flight) {
        this.state = resolveState(flight);
    }

    public void setState(FlightState state) { this.state = state; }

    public void nextState(Flight flight) {
        state.next(flight, this);
    }

    private FlightState resolveState(Flight flight) {
        if (flight == null || flight.getStatus() == null) {
            return new ScheduledState();
        }
        return switch (flight.getStatus()) {
            case "SCHEDULED" -> new ScheduledState();
            case "BOARDING" -> new BoardingState();
            case "TAXIING" -> new TaxiingState();
            case "CLEARED_FOR_TAKEOFF" -> new ClearedForTakeoffState();
            case "AIRBORNE" -> new AirborneState();
            default -> new ScheduledState();
        };
    }
}