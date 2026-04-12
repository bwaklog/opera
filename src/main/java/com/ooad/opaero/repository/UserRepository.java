package com.ooad.opaero.repository;
import com.ooad.opaero.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}