package com.ooad.opaero.repository;
import com.ooad.opaero.model.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GateRepository extends JpaRepository<Gate, String> {
    List<Gate> findByOccupiedFalse();
}