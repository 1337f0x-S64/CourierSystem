package com.example.courier.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/courierinf";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static void main(String[] args) {
        try(Connection _ = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connection successful");
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }

}
