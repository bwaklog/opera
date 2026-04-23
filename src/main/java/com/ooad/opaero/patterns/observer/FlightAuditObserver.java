package com.ooad.opaero.patterns.observer;
import com.ooad.opaero.model.Flight;
import org.springframework.stereotype.Component;

@Component
public class FlightAuditObserver implements FlightObserver {
    @Override
    public void update(Flight flight) {
        System.out.println("Flight update: " + flight.getFlightNumber() + " status=" + flight.getStatus());
    }
}
