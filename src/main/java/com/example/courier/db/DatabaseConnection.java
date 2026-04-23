package com.example.courier.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/courier_db");
    private static final String USER = System.getenv().getOrDefault("DB_USER", "localhost");
    private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "12345678");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection _ = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connection successful");
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }

}
