package com.example.courier.domain.services;

import com.example.courier.domain.valueobjects.UserRole;
import com.example.courier.models.UserGlobal;
import com.example.courier.domain.repositories.UserRepository;
import com.example.courier.utils.security.EncryptPassword;

public class AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Authenticates a user by username and password.
    public boolean authenticate(String username, String plainPassword) {
        String encryptedPassword = EncryptPassword.encryptSHA256(plainPassword);
        return userRepository.authenticate(username, encryptedPassword);
    }

    public UserRole currentUserRole() {
        String roleName = UserGlobal.getInstance().getRol();
        if (roleName == null || roleName.isBlank()) throw new IllegalStateException("No user is currently authenticated.");
        return UserRole.fromName(roleName);
    }
    public boolean currentUserCanUpdateDelivery() {
        return currentUserRole().canUpdateDeliveryStatus();
    }

    public boolean currentUserHasFullAccess() {
        return currentUserRole().hasFullAccess();
    }
}
