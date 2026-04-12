package com.ooad.opaero.repository;
import com.ooad.opaero.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByDestination(String destination);
}