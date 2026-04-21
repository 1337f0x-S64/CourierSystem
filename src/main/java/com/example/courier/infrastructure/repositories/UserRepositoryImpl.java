package com.example.courier.infrastructure.repositories;

import com.example.courier.db.Database;
import com.example.courier.db.MyUserViewsDatabase;
import com.example.courier.db.UsersDatabase;
import com.example.courier.domain.repositories.UserRepository;
import com.example.courier.models.Users;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public List<UserSummary> findAll(String nameFilter, String roleFilter) {
        return UsersDatabase.loadUsers(nameFilter, roleFilter).stream()
                .map(this::toSummary).collect(Collectors.toList());
    }

    @Override
    public Optional<UserSummary> findById(int userId) {
        Users u = UsersDatabase.loadUserId(userId);
        return Optional.ofNullable(u).map(this::toSummary);
    }

    @Override
    public boolean authenticate(String username, String encryptedPassword) {
        return Database.InitSession(username, encryptedPassword);
    }

    @Override
    public boolean save(String name, String lastname, String email,
                        String username, String encryptedPassword, int roleId) {
        return UsersDatabase.createUser(name, lastname, email, username, encryptedPassword, roleId);
    }

    @Override
    public boolean update(int userId, String name, String lastname, String email,
                          String username, String encryptedPassword, int roleId) {
        return UsersDatabase.updateUser(userId, name, lastname, email, username, encryptedPassword, roleId);
    }

    @Override
    public void updateProfile(int userId, String name, String lastName,
                              String username, String email, String encryptedPassword) {
        MyUserViewsDatabase.updateMyUser(userId, name, lastName, username, email, encryptedPassword);
    }

    @Override
    public boolean delete(int userId) {
        return UsersDatabase.deleteUser(userId);
    }

    private UserSummary toSummary(Users u) {
        return new UserSummary(u.getId(), u.getName(), u.getLastName(),
                u.getUserName(), u.getEmail(), u.getRole(), u.getRoleName());
    }
}
