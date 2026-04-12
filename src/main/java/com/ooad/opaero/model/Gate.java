package com.ooad.opaero.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Gate {
    @Id
    private String gateId;
    private String terminalProximity;
    private boolean occupied;
}