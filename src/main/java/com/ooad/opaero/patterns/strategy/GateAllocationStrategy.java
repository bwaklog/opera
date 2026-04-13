package com.ooad.opaero.patterns.strategy;
import com.ooad.opaero.model.Gate;
import java.util.List;

public interface GateAllocationStrategy {
    Gate allocateGate(List<Gate> availableGates);
}