package com.ooad.opaero.repository;
import com.ooad.opaero.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByPassengerIdAndFlightId(Long passengerId, Long flightId);
    List<Booking> findByFlightId(Long flightId);
    List<Booking> findByPassengerId(Long passengerId);
    long countByFlightId(Long flightId);
}
