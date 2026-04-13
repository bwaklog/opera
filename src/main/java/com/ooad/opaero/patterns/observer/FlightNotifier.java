package com.ooad.opaero.patterns.observer;
import com.ooad.opaero.model.Flight;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

// Observer Pattern Subject
@Component
public class FlightNotifier {
    private List<FlightObserver> observers = new ArrayList<>();
    
    public void addObserver(FlightObserver obs) { observers.add(obs); }
    public void removeObserver(FlightObserver obs) { observers.remove(obs); }
    
    public void notifyFlightUpdate(Flight flight) {
        for(FlightObserver obs: observers){
            obs.update(flight);
        }
    }
}