package com.example.courier.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MyUserViewsDatabase {

    public static void updateMyUser(int user_id, String name, String lastName, String username, String email, String password) {
        String sqlQuery = "update public.users set name = ?, last_name = ?, username = ?, email = ?, password = ? where id = ?;";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, password);
            preparedStatement.setInt(6, user_id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
