package com.ooad.opaero.repository;
import com.ooad.opaero.model.Runway;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RunwayRepository extends JpaRepository<Runway, String> {
    List<Runway> findByOccupiedFalse();
}