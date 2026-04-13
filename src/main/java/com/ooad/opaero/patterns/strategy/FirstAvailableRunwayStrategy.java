package com.ooad.opaero.patterns.strategy;
import com.ooad.opaero.model.Runway;
import java.util.List;

public class FirstAvailableRunwayStrategy implements RunwayAllocationStrategy {
    @Override
    public Runway allocateRunway(List<Runway> availableRunways) {
        if (availableRunways.isEmpty()) {
            return null;
        }
        return availableRunways.get(0);
    }
}
