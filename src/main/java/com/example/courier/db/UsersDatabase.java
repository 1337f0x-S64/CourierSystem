package com.example.courier.db;

import com.example.courier.models.Users;
import com.example.courier.utils.messages.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class UsersDatabase {
    public static ObservableList<Users> loadUsers(String searchName, String roleName) {
        String sqlQuery = "select s.*, r.name as name_role from public.users s left join public.roles r on r.id = s.role_id where CONCAT_WS(' ', s.name, s.last_name) ilike '%'|| ? ||'%' and r.name ilike '%'|| ? ||'%';";

        ObservableList<Users> users = FXCollections.observableArrayList();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {

            stmt.setString(1, searchName);
            stmt.setString(2, roleName);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String lastname = rs.getString("last_name");
                String username = rs.getString("username");
                String email = rs.getString("email");
                int role = rs.getInt("role_id");
                String role_name = rs.getString("name_role");

                users.add(new Users(id, name, lastname, username, email, role, role_name));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
    public static Users loadUserId(int id) {
        String sqlQuery = "select u.*, r.name as name_role from public.users u left join public.roles r on r.id = u.role_id where u.id = ?;";

        Users user = null;

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = cn.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int userid = rs.getInt("id");
                String name = rs.getString("name");
                String lastname = rs.getString("last_name");
                String username = rs.getString("username");
                String email = rs.getString("email");
                int role = rs.getInt("role_id");
                String role_name = rs.getString("name_role");

                user = new Users(userid, name, lastname, username, email, role, role_name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static Boolean createUser(String name, String lastname, String email, String username, String pass, int role) {
        String sqlQuery = "INSERT INTO public.users(name, last_name, username, password, email, role_id) VALUES (?, ?, ?, ?, ?, ?);";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = cn.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastname);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, pass);
            preparedStatement.setString(5, email);
            preparedStatement.setInt(6, role);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e ) {
            e.printStackTrace();
            return false;
        }

    }
    public static Boolean updateUser(int userId, String name, String lastname, String email, String username, String pass, int role) {
        String sqlQuery = "update public.users set name = ?, last_name = ?, username = ?, password = ?, email = ?, role_id = ? where id = ?;";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = cn.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastname);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, pass);
            preparedStatement.setString(5, email);
            preparedStatement.setInt(6, role);
            preparedStatement.setInt(7, userId);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e ) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean deleteUser(int userId) {
        String sqlQuery = "delete from public.users where id = ?;";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = cn.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, userId);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        }
        catch (SQLException e ) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());

            if (e.getMessage().contains("violates foreign key constraint")){

                Message.showError("Error", "An error occurred while trying to delete the user.");
            }else {
                Message.showError("Error", "Cannot delete the user because they have linked records.");
            }
            return false;
        }
    }
}

