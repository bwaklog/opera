package com.ooad.opaero.patterns.strategy;
import com.ooad.opaero.model.Runway;
import java.util.List;

public interface RunwayAllocationStrategy {
    Runway allocateRunway(List<Runway> availableRunways);
}
