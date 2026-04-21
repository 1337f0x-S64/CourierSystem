package com.example.courier.db;
import com.example.courier.models.UserGlobal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    public static boolean InitSession(String user, String password ) {
        String sqlQuery = "select u.id, u.name as name_user, u.last_name, u.username, u.password, u.email, r.name as name_rol from public.users as u inner join public.roles as r on r.id = u.role_id where username = ? and password = ?;";

        try(Connection cn = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = cn.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, user);
            preparedStatement.setString(2, password);

            try (ResultSet result = preparedStatement.executeQuery()) {
                int user_id = 0;
                String username = null;
                String pass = null;
                String firstName = null;
                String lastName = null;
                String email = null;
                String passwordDatabase = null;
                String rol = null;

                if (result.next()) {
                    user_id = result.getInt("id");
                    firstName = result.getString("name_user");
                    lastName = result.getString("last_name");
                    username = result.getString("username");
                    pass = result.getString("password");
                    email = result.getString("email");
                    passwordDatabase = result.getString("password");
                    rol = result.getString("name_rol");


                } else {
                    System.out.println("Incorrect username and/or password.");
                }

                UserGlobal.getInstance().setId(user_id);
                UserGlobal.getInstance().setName(firstName);
                UserGlobal.getInstance().setLastName(lastName);
                UserGlobal.getInstance().setUsername(username);
                UserGlobal.getInstance().setEmail(email);
                UserGlobal.getInstance().setPassword(passwordDatabase);
                UserGlobal.getInstance().setRol(rol);

                return pass != null && pass.equals(password) && username != null && username.equals(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
