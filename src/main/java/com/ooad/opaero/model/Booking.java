package com.ooad.opaero.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long passengerId;
    private Long flightId;
    private String seat;
    private String baggageTag;
    private LocalDateTime checkedInAt;
}
