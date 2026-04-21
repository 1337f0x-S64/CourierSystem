package com.example.courier.domain.repositories;

import com.example.courier.domain.valueobjects.UserRole;

import java.util.List;
import java.util.Optional;

/*Repository interface: User and Access Management bounded context.
 Abstracts persistence for the User entity.
*/
public interface UserRepository {

    record UserSummary(int id, String name, String lastname, String username,
                       String email, int roleId, String roleName) {}

    List<UserSummary> findAll(String nameFilter, String roleFilter);
    Optional<UserSummary> findById(int userId);

    // Returns true if credentials match a stored user, false otherwise
    boolean authenticate(String username, String encryptedPassword);

    boolean save(String name, String lastname, String email,
                 String username, String encryptedPassword, int roleId);
    boolean update(int userId, String name, String lastname, String email,
                   String username, String encryptedPassword, int roleId);
    void updateProfile(int userId, String name, String lastName,
                       String username, String email, String encryptedPassword);
    boolean delete(int userId);
}
