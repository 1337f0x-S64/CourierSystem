package com.example.courier.db;
import java.sql.Timestamp;
import java.sql.Date;
import com.example.courier.modelsPersonalize.Routes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.Instant;

public class DeliveryViewsDatabase {
    public static ObservableList<Routes> loadData() {
        String sql = "select r.*, p.description, concat_ws(' ', u.name, u.last_name) as name_courier from public.routes r inner join public.packages p on p.id = r.package_id inner join public.users u on u.id = r.courier_id;";
        ObservableList<Routes> routes = FXCollections.observableArrayList();

        try (Connection cn = DatabaseConnection.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int package_id = rs.getInt("package_id");
                String package_name = rs.getString("description");
                String start_location = rs.getString("start_location");
                String end_location = rs.getString("end_location");
                String tracking_number = rs.getString("tracking_number");
                Timestamp delivery_date = rs.getTimestamp("delivery_date");
                String optimal_path = rs.getString("optimal_path");
                String status = rs.getString("status");
                int courier_id = rs.getInt("courier_id");
                String courier_name = rs.getString("name_courier");
                Date possible_reception_date = rs.getDate("possible_reception_date");
                Timestamp reception_date = rs.getTimestamp("reception_date");

                routes.add(new Routes(id, package_id, package_name, start_location, end_location, tracking_number,
                        delivery_date, optimal_path, status, courier_id, courier_name, possible_reception_date, reception_date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routes;
    }
    public static Routes loadRoute(int id) {
        String sqlQuery = "select r.*, p.description, concat_ws(' ', u.name, u.last_name) as name_courier from public.routes r inner join public.packages p on p.id = r.package_id inner join public.users u on u.id = r.courier_id where r.id = ?;";

        Routes route = null;
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = cn.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int routeid = rs.getInt("id");
                int package_id = rs.getInt("package_id");
                String package_name = rs.getString("description");
                String start_location = rs.getString("start_location");
                String end_location = rs.getString("end_location");
                String tracking_number = rs.getString("tracking_number");
                Timestamp delivery_date = rs.getTimestamp("delivery_date");
                String optimal_path = rs.getString("optimal_path");
                String status = rs.getString("status");
                int courier_id = rs.getInt("courier_id");
                String courier_name = rs.getString("name_courier");
                Date possible_reception_date = rs.getDate("possible_reception_date");
                Timestamp reception_date = rs.getTimestamp("reception_date");

                route = new Routes(routeid, package_id, package_name, start_location, end_location, tracking_number,
                        delivery_date, optimal_path, status, courier_id, courier_name, possible_reception_date, reception_date);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return route;
    }
    public static Boolean createRoute(int packageId, String startLocation, String endLocation, String trackingNumber, String optimalPath, String status, int courierId, Timestamp deliveryDate, Timestamp possibleReceptionDate) {
        String sqlQuery = "INSERT INTO public.routes (package_id, start_location, end_location, tracking_number, delivery_date, optimal_path, status, courier_id, possible_reception_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = cn.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, packageId);
            preparedStatement.setString(2, startLocation);
            preparedStatement.setString(3, endLocation);
            preparedStatement.setString(4, trackingNumber);
            preparedStatement.setTimestamp(5, deliveryDate);
            preparedStatement.setString(6, optimalPath);
            preparedStatement.setString(7, status);
            preparedStatement.setInt(8, courierId);
            preparedStatement.setTimestamp(9, possibleReceptionDate);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static Boolean updateRoute(int id, int packageId, String startLocation, String endLocation, String trackingNumber, String optimalPath, String status, int courierId, Timestamp possibleReceptionDate, Timestamp deliveryDate) {
        String sqlQuery = "UPDATE public.routes SET package_id = ?, start_location = ?, end_location = ?, tracking_number = ?, delivery_date = ?, optimal_path = ?, status = ?, courier_id = ?, possible_reception_date = ? WHERE id = ?;";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = cn.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, packageId);
            preparedStatement.setString(2, startLocation);
            preparedStatement.setString(3, endLocation);
            preparedStatement.setString(4, trackingNumber);
            preparedStatement.setTimestamp(5, deliveryDate);
            preparedStatement.setString(6, optimalPath);
            preparedStatement.setString(7, status);
            preparedStatement.setInt(8, courierId);
            preparedStatement.setTimestamp(9, possibleReceptionDate);
            preparedStatement.setInt(10, id);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static Boolean deleteRoute(int id) {
        String sqlQuery = "DELETE FROM public.routes WHERE id = ?;";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = cn.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static Boolean completeRoute(int id) {
        String sqlQuery = "update public.routes r set reception_date = ?, status = ? where r.id = ?;";

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = cn.prepareStatement(sqlQuery)) {

            preparedStatement.setTimestamp(1, Timestamp.from(Instant.now()));
            preparedStatement.setString(2, "d");
            preparedStatement.setInt(3, id);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
