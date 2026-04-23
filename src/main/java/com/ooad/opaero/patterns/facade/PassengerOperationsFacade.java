package com.ooad.opaero.patterns.facade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ooad.opaero.repository.FlightRepository;
import com.ooad.opaero.repository.BookingRepository;
import com.ooad.opaero.model.Booking;
import com.ooad.opaero.model.Flight;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PassengerOperationsFacade {
    private static final int SEATS_PER_FLIGHT = 180;
    private static final int SEATS_PER_ROW = 6;

    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;

    public PassengerOperationsFacade(FlightRepository flightRepo, BookingRepository bookingRepo) {
        this.flightRepository = flightRepo;
        this.bookingRepository = bookingRepo;
    }

    @Transactional
    public boolean performCheckIn(Long passengerId, Long flightId) {
        Flight f = flightRepository.findById(flightId).orElse(null);
        if (f == null || !"SCHEDULED".equals(f.getStatus())) {
            return false;
        }

        Optional<Booking> existing = bookingRepository.findByPassengerIdAndFlightId(passengerId, flightId);
        if (existing.isPresent()) {
            Booking b = existing.get();
            System.out.println("Check-in (already checked in): passenger=" + passengerId
                + " flight=" + f.getFlightNumber()
                + " seat=" + b.getSeat()
                + " baggageTag=" + b.getBaggageTag());
            return true;
        }

        String seat = allocateSeat(flightId);
        if (seat == null) {
            System.out.println("Check-in failed (flight full): passenger=" + passengerId
                + " flight=" + f.getFlightNumber());
            return false;
        }

        String baggageTag = generateBaggageTag(f);

        Booking booking = new Booking();
        booking.setPassengerId(passengerId);
        booking.setFlightId(flightId);
        booking.setSeat(seat);
        booking.setBaggageTag(baggageTag);
        booking.setCheckedInAt(LocalDateTime.now());
        bookingRepository.save(booking);

        System.out.println("Baggage logged: passenger=" + passengerId
            + " flight=" + f.getFlightNumber()
            + " destination=" + f.getDestination()
            + " tag=" + baggageTag);
        System.out.println("Check-in: passenger=" + passengerId
            + " flight=" + f.getFlightNumber()
            + " seat=" + seat
            + " baggageTag=" + baggageTag);
        return true;
    }

    private String allocateSeat(Long flightId) {
        long taken = bookingRepository.countByFlightId(flightId);
        long n = taken + 1;
        if (n > SEATS_PER_FLIGHT) {
            return null;
        }
        long row = ((n - 1) / SEATS_PER_ROW) + 1;
        char col = (char) ('A' + ((n - 1) % SEATS_PER_ROW));
        return row + String.valueOf(col);
    }

    private String generateBaggageTag(Flight flight) {
        return "BG-" + flight.getFlightNumber() + "-"
            + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
