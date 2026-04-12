package com.ooad.opaero.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Runway {
    @Id
    private String runwayId;
    private double length;
    private boolean occupied;
}