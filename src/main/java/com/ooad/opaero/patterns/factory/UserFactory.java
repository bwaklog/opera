package com.ooad.opaero.patterns.factory;
import com.ooad.opaero.model.User;
import org.springframework.stereotype.Component;

// Factory Method Pattern
@Component
public class UserFactory {
    public User createUser(String role, String username) {
        User user = new User();
        user.setUsername(username);
        user.setRole(role.toUpperCase());
        return user;
    }
}