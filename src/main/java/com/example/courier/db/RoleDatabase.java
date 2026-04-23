package com.example.courier.db;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDatabase {
    public static List<String> loadRoles() {
        List<String> roles = new ArrayList<>();
        String sqlQuery = "select * from public.roles";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery)) {

            while (rs.next()) {
                roles.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }
    public static int getIdByName(String name) {
        String sqlQuery = "select id from public.roles where name = ?";

        int idRol = -1;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {

            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idRol = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idRol;
    }
    public static String getIdById(int id) {
        String sqlQuery = "select name from public.roles where id = ?;";

        String Rol = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Rol = rs.getString("name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Rol;
    }
}
