package com.ooad.opaero.patterns.strategy;
import com.ooad.opaero.model.Gate;
import java.util.List;

// Simple implementation that sorts by proximity (mocked by string length for sim)
public class IntelligentGateStrategy implements GateAllocationStrategy {
    @Override
    public Gate allocateGate(List<Gate> availableGates) {
        if(availableGates.isEmpty()) return null;
        return availableGates.stream().findFirst().orElse(null);
    }
}